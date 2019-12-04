package http.wrapper

import java.io.*
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper


class RequestWrapper(httpServletRequest: HttpServletRequest): HttpServletRequestWrapper(httpServletRequest) {
    private val body: String
        get() = field
    init {
        var stringBuilder = StringBuilder()
        var bufferedReader: BufferedReader? = null
        try {
            val servletInputStream = httpServletRequest.inputStream
            if(servletInputStream!=null){
                bufferedReader = BufferedReader(InputStreamReader(servletInputStream))
                val charBuffer = charArrayOf()
                var bytesRead = -1
                do {
                    bytesRead = bufferedReader.read(charBuffer)
                    stringBuilder.append(charBuffer, 0, bytesRead)
                }while (bytesRead > 0)
            }else{
                stringBuilder.append("")
            }
        }catch (ex: Exception){

        }finally {
            if(bufferedReader!=null){
                try {
                    bufferedReader.close()
                }catch (ex: IOException){

                }
            }
        }
        this.body = stringBuilder.toString()
    }

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream? {
        val byteArrayInputStream = ByteArrayInputStream(this.body.toByteArray())
        return object : ServletInputStream() {
            @Throws(IOException::class)
            override fun read(): Int {
                return byteArrayInputStream.read()
            }
        }
    }

    @Throws(IOException::class)
    override fun getReader(): BufferedReader? {
        return BufferedReader(InputStreamReader(inputStream))
    }
}