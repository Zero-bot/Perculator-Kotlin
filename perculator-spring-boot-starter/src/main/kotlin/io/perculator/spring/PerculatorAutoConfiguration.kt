package io.perculator.spring

import io.perculator.core.Perculator
import io.perculator.core.PerculatorConfig
import io.perculator.core.filter.PerculatorFilter
import io.perculator.core.loader.JsonRuleLoader
import io.perculator.core.loader.RuleLoader
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ResourceLoader
import java.nio.file.Paths

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(PerculatorFilter::class)
@EnableConfigurationProperties(PerculatorProperties::class)
@ConditionalOnProperty(prefix = "perculator", name = ["enabled"], havingValue = "true", matchIfMissing = true)
public class PerculatorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public fun ruleLoader(
        properties: PerculatorProperties,
        resourceLoader: ResourceLoader,
    ): RuleLoader {
        val resource = resourceLoader.getResource(properties.rulesPath)
        val path = if (resource.isFile) {
            resource.file.toPath()
        } else {
            Paths.get(properties.rulesPath)
        }
        return JsonRuleLoader(path)
    }

    @Bean
    @ConditionalOnMissingBean
    public fun perculator(
        ruleLoader: RuleLoader,
        properties: PerculatorProperties,
    ): Perculator {
        return Perculator.create(ruleLoader)
    }

    @Bean
    public fun perculatorFilterRegistration(
        perculator: Perculator,
        properties: PerculatorProperties,
    ): FilterRegistrationBean<PerculatorFilter> {
        val registration = FilterRegistrationBean(PerculatorFilter(perculator))
        registration.order = properties.filterOrder
        registration.urlPatterns = properties.urlPatterns
        return registration
    }
}
