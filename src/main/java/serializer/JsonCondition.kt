package serializer

import affirm.rule.Condition
import affirm.rule.json.handler.*
import kotlinx.serialization.Serializable
import http.Http as HttpProtocol
import http.Parameters as HttpParameters
import http.Cookies as HttpCookies
import http.Headers as HttpHeaders
import http.Request as HttpRequest

@Serializable
data class JsonCondition(val location: String, val operator: String, val key: String? = null, val value: String? = null) {

    fun generateConditionFromJsonObject(): Condition {
        val (locationValue, operatorValue) = setConditionProperties()
        return if(key != null && value != null) Condition(locationValue, operatorValue, key, value)
        else if(key != null ) Condition(locationValue, operatorValue, key)
        else Condition(locationValue, operatorValue )
    }

    private fun setConditionProperties(): Pair<Byte, Byte> {
        val locationValue = setLocationValue()
        val operatorValue = when(locationValue){
            HttpProtocol.Parameter -> setParameterOperatorValue()
            HttpProtocol.Request -> setRequestOperatorValue()
            HttpProtocol.Header -> setHeaderOperatorValue()
            HttpProtocol.Cookie -> setCookieOperatorValue()
            else -> -1
        }
        return Pair(locationValue, operatorValue)
    }

    private fun setLocationValue(): Byte{
        return when(location){
            Http.Parameter.value -> HttpProtocol.Parameter
            Http.Cookie.value -> HttpProtocol.Cookie
            Http.Header.value -> HttpProtocol.Header
            Http.Request.value -> HttpProtocol.Request
            else -> -1
        }
    }

    private fun setParameterOperatorValue(): Byte{
        return when(operator){
            Parameter.HasParameter.value -> HttpParameters.hasParameter
            Parameter.MatchParameter.value -> HttpParameters.matchParameter
            Parameter.HasParameterWithValue.value -> HttpParameters.hasParameterWithValue
            Parameter.MatchParameterWithValue.value -> HttpParameters.matchParameterWithValue
            Parameter.Match.value -> HttpParameters.match
            Parameter.CountParameters.value -> HttpParameters.countParameters
            Parameter.ValueHasOnlyAlphaNumericChar.value -> HttpParameters.valueHasOnlyAlphaNumericChar
            else -> -1
        }
    }

    private fun setCookieOperatorValue():Byte{
        return when(operator){
            Cookie.HasCookie.value -> HttpCookies.hasCookie
            Cookie.CookieHasValue.value -> HttpCookies.cookieHasValue
            Cookie.MatchCookie.value -> HttpCookies.matchCookie
            Cookie.MatchValue.value -> HttpCookies.matchValue
            Cookie.CountCookies.value -> HttpCookies.countCookies
            Cookie.ValueHasOnlyAlphaNumericChar.value -> HttpCookies.valueHasOnlyAlphaNumericChar
            Cookie.ValueLengthEquals.value -> HttpCookies.valueLengthEquals
            else -> -1
        }
    }

    private fun setHeaderOperatorValue(): Byte{
        return when(operator){
            Header.HasHeader.value -> HttpHeaders.hasHeader
            Header.HeaderHasValue.value -> HttpHeaders.headerHasValue
            Header.MatchHeader.value -> HttpHeaders.matchHeader
            Header.MatchHeaderValue.value -> HttpHeaders.matchHeaderValue
            Header.IsHeaderRepeated.value -> HttpHeaders.isHeaderRepeated
            Header.HeaderHasSpecialChar.value -> HttpHeaders.headerHasSpecialChar
            Header.TotalHeadersCountEquals.value -> HttpHeaders.totalHeadersCountEquals
            Header.HeaderCountEquals.value -> HttpHeaders.headerCountEquals
            Header.MatchAllHeadersValue.value -> HttpHeaders.matchAllHeadersValue
            else -> -1
        }
    }

    private fun setRequestOperatorValue():Byte{
        return when(operator){
            Request.MethodEquals.value -> HttpRequest.methodEquals
            Request.MethodNotEquals.value -> HttpRequest.methodNotEquals
            Request.IsGetRequest.value -> HttpRequest.isGetRequest
            Request.IsPostRequest.value -> HttpRequest.isPostRequest
            Request.IsHeadRequest.value -> HttpRequest.isHeadRequest
            Request.IsPutRequest.value -> HttpRequest.isPutRequest
            Request.IsDeleteRequest.value -> HttpRequest.isDeleteRequest
            Request.IsConnectRequest.value -> HttpRequest.isConnectRequest
            Request.IsOptionsRequest.value -> HttpRequest.isOptionsRequest
            Request.IsTraceRequest.value -> HttpRequest.isTraceRequest
            Request.IsPatchRequest.value -> HttpRequest.isPatchRequest
            Request.QueryStringEquals.value -> HttpRequest.queryStringEquals
            Request.QueryStringNotEquals.value -> HttpRequest.queryStringNotEquals
            Request.QueryStringContains.value -> HttpRequest.queryStringContains
            Request.QueryStringNotContains.value -> HttpRequest.queryStringNotContains
            Request.QueryStringMatch.value -> HttpRequest.queryStringMatch
            Request.QueryStringNotMatch.value -> HttpRequest.queryStringNotMatch
            Request.UriEquals.value -> HttpRequest.uriEquals
            Request.UriNotEquals.value -> HttpRequest.uriNotEquals
            Request.UriContains.value -> HttpRequest.uriContains
            Request.UriNotContains.value -> HttpRequest.uriNotContains
            Request.UriMatch.value -> HttpRequest.uriMatch
            Request.UriNotMatch.value -> HttpRequest.uriNotMatch
            Request.SchemeEquals.value -> HttpRequest.schemeEquals
            Request.SchemeNotEquals.value -> HttpRequest.schemeNotEquals
            Request.ProtocolEquals.value -> HttpRequest.protocolEquals
            Request.ProtocolNotEquals.value -> HttpRequest.protocolNotEquals
            Request.ProtocolMatch.value -> HttpRequest.protocolMatch
            Request.ProtocolNotMatch.value -> HttpRequest.protocolNotMatch
            Request.UrlEquals.value -> HttpRequest.urlEquals
            Request.UrlNotEquals.value -> HttpRequest.urlNotEquals
            Request.UrlMatch.value -> HttpRequest.urlMatch
            Request.UrlNotMatch.value -> HttpRequest.urlNotMatch
            Request.UrlContains.value -> HttpRequest.urlContains
            Request.UrlNotContains.value -> HttpRequest.urlNotContains
            Request.ServerEquals.value -> HttpRequest.serverEquals
            Request.ServerNotEquals.value -> HttpRequest.serverNotEquals
            Request.ServerMatch.value -> HttpRequest.serverMatch
            Request.ServerNotMatch.value -> HttpRequest.serverNotMatch
            Request.PortEquals.value -> HttpRequest.portEquals
            Request.PortNotEquals.value -> HttpRequest.portNotEquals
            else -> -1
        }
    }

}
