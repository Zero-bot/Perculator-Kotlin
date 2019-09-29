package main.http

import java.util.*
import javax.servlet.http.HttpServletRequest


class Http(private val httpServletRequest: HttpServletRequest) {
    val parameters:Parameters = Parameters(this.httpServletRequest)

    companion object{
        final val Parameter: Byte = 0
        final val Cookie: Byte = 1
        final val Header: Byte = 2
    }

    init {
        val id: String = UUID.randomUUID().toString()
    }

}