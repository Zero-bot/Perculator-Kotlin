package io.perculator.core.loader

import io.perculator.core.model.Rule
import org.slf4j.LoggerFactory
import java.nio.file.*
import java.time.Duration
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread

public class FileWatchingRuleLoader(
    private val delegate: RuleLoader,
    private val path: Path,
    private val pollInterval: Duration = Duration.ofSeconds(5),
) : RuleLoader, AutoCloseable {

    private val logger = LoggerFactory.getLogger(FileWatchingRuleLoader::class.java)
    private val cachedRules = AtomicReference<List<Rule>>()

    @Volatile
    private var running = true

    private val watchThread: Thread

    init {
        cachedRules.set(delegate.load())
        logger.info("Loaded {} rules from {}", cachedRules.get().size, path)

        watchThread = thread(isDaemon = true, name = "perculator-rule-watcher") {
            try {
                val watchService = path.parent.fileSystem.newWatchService()
                path.parent.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)

                while (running) {
                    val key = watchService.poll(pollInterval.toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS)
                    if (key != null) {
                        for (event in key.pollEvents()) {
                            val changed = event.context() as? Path
                            if (changed != null && path.endsWith(changed)) {
                                logger.info("Rules file changed, reloading: {}", path)
                                try {
                                    cachedRules.set(delegate.load())
                                    logger.info("Reloaded {} rules", cachedRules.get().size)
                                } catch (e: Exception) {
                                    logger.error("Failed to reload rules", e)
                                }
                            }
                        }
                        key.reset()
                    }
                }
                watchService.close()
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            } catch (e: Exception) {
                logger.error("Rule watcher thread terminated unexpectedly", e)
            }
        }
    }

    override fun load(): List<Rule> = cachedRules.get()

    override fun close() {
        running = false
        watchThread.interrupt()
        watchThread.join(pollInterval.toMillis() * 2)
    }
}
