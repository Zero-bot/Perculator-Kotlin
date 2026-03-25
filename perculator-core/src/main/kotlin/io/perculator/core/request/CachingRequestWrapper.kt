package io.perculator.core.request

import io.perculator.core.exception.RequestBodyTooLargeException
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

public class CachingRequestWrapper(
    request: HttpServletRequest,
    private val maxBodySize: Int = DEFAULT_MAX_BODY_SIZE,
) : HttpServletRequestWrapper(request) {

    private val cachedBody: ByteArray

    init {
        val inputStream = request.inputStream
        if (inputStream != null) {
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            val result = java.io.ByteArrayOutputStream()
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                result.write(buffer, 0, bytesRead)
                if (result.size() > maxBodySize) {
                    throw RequestBodyTooLargeException(maxBodySize)
                }
            }
            cachedBody = result.toByteArray()
        } else {
            cachedBody = ByteArray(0)
        }
    }

    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(cachedBody)
        return object : ServletInputStream() {
            override fun read(): Int = byteArrayInputStream.read()
            override fun isFinished(): Boolean = byteArrayInputStream.available() == 0
            override fun isReady(): Boolean = true
            override fun setReadListener(listener: ReadListener?) {
                throw UnsupportedOperationException("setReadListener is not supported")
            }
        }
    }

    override fun getReader(): BufferedReader =
        BufferedReader(InputStreamReader(inputStream, characterEncoding ?: "UTF-8"))

    public fun getBody(): String = String(cachedBody, charset(characterEncoding ?: "UTF-8"))

    public companion object {
        public const val DEFAULT_MAX_BODY_SIZE: Int = 512 * 1024
        private const val DEFAULT_BUFFER_SIZE: Int = 4096
    }
}
