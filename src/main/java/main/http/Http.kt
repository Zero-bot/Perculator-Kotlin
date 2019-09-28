package main.http

import java.util.*
import javax.servlet.http.HttpServletRequest


class Http(private val httpServletRequest: HttpServletRequest) {
    val parameters:Parameters = Parameters(this.httpServletRequest)
    init {
        val id: String = UUID.randomUUID().toString()
    }

}