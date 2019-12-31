package http

import http.wrapper.RequestWrapper
import java.util.*


class Http(private val httpServletRequest: RequestWrapper) {

    companion object{
        const val Parameter: Byte = 0
        const val Cookie: Byte = 1
        const val Header: Byte = 2
        const val Request: Byte = 3
    }

    init {
        val id: String = UUID.randomUUID().toString()
    }

}