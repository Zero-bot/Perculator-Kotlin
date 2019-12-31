package wrappers

import java.io.*
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

class MutableHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private val body: String

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(body.toByteArray())
        return object : ServletInputStream() {
            @Throws(IOException::class)
            override fun read(): Int {
                return byteArrayInputStream.read()
            }
        }
    }

    @Throws(IOException::class)
    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(this.inputStream))
    }

    fun getBody() = body

    init {
        val stringBuilder = StringBuilder()
        var bufferedReader: BufferedReader? = null
        try {
            val inputStream: InputStream? = request.inputStream
            if (inputStream != null) {
                bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val charBuffer = CharArray(128)
                var bytesRead = -1
                while (bufferedReader.read(charBuffer).also { bytesRead = it } > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead)
                }
            } else {
                stringBuilder.append("")
            }
        } catch (ex: IOException) {
            throw ex
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close()
                } catch (ex: IOException) {
                    throw ex
                }
            }
        }
        body = stringBuilder.toString()
    }
}