package http


import exception.KeyCannotBeNullException
import exception.OperatorNotSupportedException
import javax.servlet.http.HttpServletRequest

class Request(httpServletRequest: HttpServletRequest) {
    
    private val method: String = httpServletRequest.method
    private val queryString: String? = httpServletRequest.queryString
    private val uri: String = httpServletRequest.requestURI
    private val scheme: String = httpServletRequest.scheme
    private val protocol: String = httpServletRequest.protocol
    private val url: String = httpServletRequest.requestURL.toString()
    private val server: String = httpServletRequest.serverName
    private val port: Int = httpServletRequest.serverPort

    companion object{
        const val methodEquals:Byte = 0
        const val methodNotEquals:Byte = 1
        const val isGetRequest:Byte = 2
        const val isPostRequest:Byte = 3
        const val isHeadRequest:Byte = 4
        const val isPutRequest:Byte = 5
        const val isDeleteRequest:Byte = 6
        const val isConnectRequest:Byte = 7
        const val isOptionsRequest:Byte = 8
        const val isTraceRequest:Byte = 9
        const val isPatchRequest:Byte = 10

        const val queryStringEquals:Byte = 11
        const val queryStringNotEquals:Byte = 12
        const val queryStringContains:Byte = 13
        const val queryStringNotContains:Byte = 14
        const val queryStringMatch:Byte = 15
        const val queryStringNotMatch:Byte = 16

        const val uriEquals:Byte = 17
        const val uriNotEquals:Byte = 18
        const val uriContains:Byte = 19
        const val uriNotContains:Byte = 20
        const val uriMatch:Byte = 21
        const val uriNotMatch:Byte = 22

        const val schemeEquals:Byte = 23
        const val schemeNotEquals:Byte = 24

        const val protocolEquals:Byte = 25
        const val protocolNotEquals: Byte = 26
        const val protocolMatch: Byte = 27
        const val protocolNotMatch: Byte = 28

        const val urlEquals:Byte = 29
        const val urlNotEquals:Byte = 30
        const val urlMatch: Byte = 31
        const val urlNotMatch: Byte = 32
        const val urlContains: Byte = 33
        const val urlNotContains: Byte = 34

        const val serverEquals:Byte = 35
        const val serverNotEquals:Byte = 36
        const val serverMatch: Byte = 37
        const val serverNotMatch: Byte = 38

        const val portEquals: Byte = 39
        const val portNotEquals: Byte = 40

    }

    private fun methodEquals(key: String) = key.equals(method, true)
    private fun methodNotEquals(key: String) = !key.equals(method, true)
    private fun isGetRequest() = "GET".equals(method, true)
    private fun isPostRequest() = "POST".equals(method, true)
    private fun isHeadRequest() = "HEAD".equals(method, true)
    private fun isPutRequest() = "PUT".equals(method, true)
    private fun isDeleteRequest() = "DELETE".equals(method, true)
    private fun isConnectRequest() = "CONNECT".equals(method, true)
    private fun isOptionsRequest() = "OPTIONS".equals(method, true)
    private fun isTraceRequest() = "TRACE".equals(method, true)
    private fun isPatchRequest() = "PATCH".equals(method, true)
    
    private fun queryStringEquals(key: String) = key == queryString
    private fun queryStringNotEquals(key: String) = key != queryString
    private fun queryStringContains(key: String): Boolean {
        if(!queryString.isNullOrEmpty()) return queryString.contains(key)
        throw NullPointerException("Query String is null")
    }
    private fun queryStringNotContains(key: String): Boolean {
        if(!queryString.isNullOrEmpty()) return !queryString.contains(key)
        throw NullPointerException("Query String is null")
    }
    private fun queryStringMatch(regex: Regex): Boolean {
        if(!queryString.isNullOrEmpty()) return regex.containsMatchIn(queryString)
        throw NullPointerException("Query String is null")
    }
    private fun queryStringNotMatch(regex: Regex): Boolean {
        if(!queryString.isNullOrEmpty()) return !regex.containsMatchIn(queryString)
        throw NullPointerException("Query String is null")
    }

    private fun uriEquals(key: String) = uri == key
    private fun uriNotEquals(key: String) = uri != key
    private fun uriContains(key: String) = uri.contains(key)
    private fun uriNotContains(key: String) = !uri.contains(key)
    private fun uriMatch(regex: Regex) = regex.containsMatchIn(uri)
    private fun uriNotMatch(regex: Regex) = !regex.containsMatchIn(uri)
    
    private fun schemeEquals(key: String) = key == scheme
    private fun schemeNotEquals(key: String) = key != scheme

    private fun protocolEquals(key: String) = protocol == key
    private fun protocolNotEquals(key: String) = protocol != key
    private fun protocolMatch(regex: Regex) = regex.containsMatchIn(protocol)
    private fun protocolNotMatch(regex: Regex) = !regex.containsMatchIn(protocol)
    
    private fun urlEquals(key: String) = url == key
    private fun urlNotEquals(key: String) = url != key
    private fun urlMatch(regex: Regex) = regex.containsMatchIn(url)
    private fun urlNotMatch(regex: Regex) = regex.containsMatchIn(url)
    private fun urlContains(key: String) = url.contains(key)
    private fun urlNotContains(key: String) = !url.contains(key)

    private fun serverEquals(key: String) = server == key
    private fun serverNotEquals(key: String) = server != key
    private fun serverMatch(regex: Regex) = regex.containsMatchIn(server)
    private fun serverNotMatch(regex: Regex) = !regex.containsMatchIn(server)

    private fun portEquals(key: Int) = key == port
    private fun portNotEquals(key: Int) = key != port


    fun evaluate(operator: Byte, key: String?):Boolean{
        return when(operator){
            methodEquals -> if (key != null ) methodEquals(key) else throw KeyCannotBeNullException("Request.methodEquals")
            methodNotEquals -> if (key != null ) methodNotEquals(key) else throw KeyCannotBeNullException("Request.methodNotEquals")
            isGetRequest -> isGetRequest()
            isPostRequest -> isPostRequest()
            isHeadRequest -> isHeadRequest()
            isPutRequest -> isPutRequest()
            isDeleteRequest -> isDeleteRequest()
            isConnectRequest -> isConnectRequest()
            isOptionsRequest -> isOptionsRequest()
            isTraceRequest -> isTraceRequest()
            isPatchRequest -> isPatchRequest()

            queryStringEquals -> if (key != null ) queryStringEquals(key) else throw KeyCannotBeNullException("Request.queryStringEquals")
            queryStringNotEquals -> if (key != null ) queryStringNotEquals(key) else throw KeyCannotBeNullException("Request.queryStringNotEquals")
            queryStringContains -> if (key != null ) queryStringContains(key) else throw KeyCannotBeNullException("Request.queryStringContains")
            queryStringNotContains -> if (key != null ) queryStringNotContains(key) else throw KeyCannotBeNullException("Request.queryStringNotContains")
            queryStringMatch -> if (key != null ) queryStringMatch(key.toRegex()) else throw KeyCannotBeNullException("Request.queryStringMatch")
            queryStringNotMatch -> if (key != null ) queryStringNotMatch(key.toRegex()) else throw KeyCannotBeNullException("Request.queryStringMatch")

            uriEquals -> if (key!=null ) uriEquals(key) else throw KeyCannotBeNullException("Request.uriEquals")
            uriNotEquals -> if (key!=null ) uriNotEquals(key) else throw KeyCannotBeNullException("Request.uriNotEquals")
            uriContains -> if (key!=null ) uriContains(key) else throw KeyCannotBeNullException("Request.uriContains")
            uriNotContains -> if (key!=null ) uriNotContains(key) else throw KeyCannotBeNullException("Request.uriNotContains")
            uriMatch -> if (key!=null ) uriMatch(key.toRegex()) else throw KeyCannotBeNullException("Request.uriMatch")
            uriNotMatch -> if (key!=null ) uriNotMatch(key.toRegex()) else throw KeyCannotBeNullException("Request.uriNotMatch")

            schemeEquals -> if (key!=null ) schemeEquals(key) else throw KeyCannotBeNullException("Request.schemeEquals")
            schemeNotEquals -> if (key!=null ) schemeNotEquals(key) else throw KeyCannotBeNullException("Request.schemeNotEquals")

            protocolEquals -> if (key!=null ) protocolEquals(key) else throw KeyCannotBeNullException("Request.protocolEquals")
            protocolNotEquals -> if (key!=null ) protocolNotEquals(key) else throw KeyCannotBeNullException("Request.protocolNotEquals")
            protocolMatch -> if (key!=null ) protocolMatch(key.toRegex()) else throw KeyCannotBeNullException("Request.protocolMatch")
            protocolNotMatch -> if (key!=null ) protocolNotMatch(key.toRegex()) else throw KeyCannotBeNullException("Request.protocolNotMatch")

            urlEquals -> if (key!=null ) urlEquals(key) else throw KeyCannotBeNullException("Request.urlEquals")
            urlNotEquals -> if (key!=null ) urlNotEquals(key) else throw KeyCannotBeNullException("Request.urlNotEquals")
            urlMatch -> if (key!=null ) urlMatch(key.toRegex()) else throw KeyCannotBeNullException("Request.urlMatch")
            urlNotMatch -> if (key!=null ) urlNotMatch(key.toRegex()) else throw KeyCannotBeNullException("Request.urlNotMatch")
            urlContains -> if (key!=null ) urlContains(key) else throw KeyCannotBeNullException("Request.urlContains")
            urlNotContains -> if (key!=null ) urlNotContains(key) else throw KeyCannotBeNullException("Request.urlNotContains")

            serverEquals -> if (key!=null ) serverEquals(key) else throw KeyCannotBeNullException("Request.serverEquals")
            serverNotEquals -> if (key!=null ) serverNotEquals(key) else throw KeyCannotBeNullException("Request.serverNotEquals")
            serverMatch -> if (key!=null ) serverMatch(key.toRegex()) else throw KeyCannotBeNullException("Request.serverMatch")
            serverNotMatch -> if (key!=null ) serverNotMatch(key.toRegex()) else throw KeyCannotBeNullException("Request.serverNotMatch")

            portEquals -> if (key!=null ) portEquals(key.toInt()) else throw KeyCannotBeNullException("Request.portEquals")
            portNotEquals -> if (key!=null ) portNotEquals(key.toInt()) else throw KeyCannotBeNullException("Request.portNotEquals")
            else -> throw OperatorNotSupportedException(operator, "Request")
        }
    }
}
