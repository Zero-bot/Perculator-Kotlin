package http

import java.util.*
import javax.servlet.http.HttpServletRequest


class Http(private val httpServletRequest: HttpServletRequest) {
    val parameters: Parameters = Parameters(this.httpServletRequest)

    companion object{
        const val Parameter: Byte = 0
        const val Cookie: Byte = 1
        const val Header: Byte = 2
    }

    init {
        val id: String = UUID.randomUUID().toString()
    }

}