package io.perculator.core.model

import io.perculator.core.exception.InvalidConditionException

public sealed class Operator {

    public sealed class ParameterOp : Operator() {
        public data object HasParameter : ParameterOp()
        public data object MatchParameter : ParameterOp()
        public data object HasParameterWithValue : ParameterOp()
        public data object MatchParameterWithValue : ParameterOp()
        public data object Match : ParameterOp()
        public data object CountParameters : ParameterOp()
        public data object ValueHasOnlyAlphaNumericChar : ParameterOp()
    }

    public sealed class CookieOp : Operator() {
        public data object HasCookie : CookieOp()
        public data object CookieHasValue : CookieOp()
        public data object MatchCookie : CookieOp()
        public data object MatchValue : CookieOp()
        public data object CountCookies : CookieOp()
        public data object ValueHasOnlyAlphaNumericChar : CookieOp()
        public data object ValueLengthEquals : CookieOp()
    }

    public sealed class HeaderOp : Operator() {
        public data object HasHeader : HeaderOp()
        public data object HeaderHasValue : HeaderOp()
        public data object MatchHeader : HeaderOp()
        public data object MatchHeaderValue : HeaderOp()
        public data object IsHeaderRepeated : HeaderOp()
        public data object HeaderHasSpecialChar : HeaderOp()
        public data object TotalHeadersCountEquals : HeaderOp()
        public data object HeaderCountEquals : HeaderOp()
        public data object MatchAllHeadersValue : HeaderOp()
    }

    public sealed class RequestOp : Operator() {
        public data object MethodEquals : RequestOp()
        public data object MethodNotEquals : RequestOp()
        public data object IsGetRequest : RequestOp()
        public data object IsPostRequest : RequestOp()
        public data object IsHeadRequest : RequestOp()
        public data object IsPutRequest : RequestOp()
        public data object IsDeleteRequest : RequestOp()
        public data object IsConnectRequest : RequestOp()
        public data object IsOptionsRequest : RequestOp()
        public data object IsTraceRequest : RequestOp()
        public data object IsPatchRequest : RequestOp()
        public data object QueryStringEquals : RequestOp()
        public data object QueryStringNotEquals : RequestOp()
        public data object QueryStringContains : RequestOp()
        public data object QueryStringNotContains : RequestOp()
        public data object QueryStringMatch : RequestOp()
        public data object QueryStringNotMatch : RequestOp()
        public data object UriEquals : RequestOp()
        public data object UriNotEquals : RequestOp()
        public data object UriContains : RequestOp()
        public data object UriNotContains : RequestOp()
        public data object UriMatch : RequestOp()
        public data object UriNotMatch : RequestOp()
        public data object SchemeEquals : RequestOp()
        public data object SchemeNotEquals : RequestOp()
        public data object ProtocolEquals : RequestOp()
        public data object ProtocolNotEquals : RequestOp()
        public data object ProtocolMatch : RequestOp()
        public data object ProtocolNotMatch : RequestOp()
        public data object UrlEquals : RequestOp()
        public data object UrlNotEquals : RequestOp()
        public data object UrlMatch : RequestOp()
        public data object UrlNotMatch : RequestOp()
        public data object UrlContains : RequestOp()
        public data object UrlNotContains : RequestOp()
        public data object ServerEquals : RequestOp()
        public data object ServerNotEquals : RequestOp()
        public data object ServerMatch : RequestOp()
        public data object ServerNotMatch : RequestOp()
        public data object PortEquals : RequestOp()
        public data object PortNotEquals : RequestOp()
    }

    public companion object {
        public fun fromString(location: Location, value: String): Operator = when (location) {
            is Location.Parameter -> parameterOpFromString(value)
            is Location.Cookie -> cookieOpFromString(value)
            is Location.Header -> headerOpFromString(value)
            is Location.Request -> requestOpFromString(value)
        }

        private fun parameterOpFromString(value: String): ParameterOp = when (value) {
            "Parameter.hasParameter", "hasParameter" -> ParameterOp.HasParameter
            "Parameter.matchParameter", "matchParameter" -> ParameterOp.MatchParameter
            "Parameter.hasParameterWithValue", "hasParameterWithValue" -> ParameterOp.HasParameterWithValue
            "Parameter.matchParameterWithValue", "matchParameterWithValue" -> ParameterOp.MatchParameterWithValue
            "Parameter.match", "match" -> ParameterOp.Match
            "Parameter.countParameters", "countParameters" -> ParameterOp.CountParameters
            "Parameter.valueHasOnlyAlphaNumericChar", "valueHasOnlyAlphaNumericChar" -> ParameterOp.ValueHasOnlyAlphaNumericChar
            else -> throw InvalidConditionException("Unknown parameter operator: $value")
        }

        private fun cookieOpFromString(value: String): CookieOp = when (value) {
            "Cookie.hasCookie", "hasCookie" -> CookieOp.HasCookie
            "Cookie.cookieHasValue", "cookieHasValue" -> CookieOp.CookieHasValue
            "Cookie.matchCookie", "matchCookie" -> CookieOp.MatchCookie
            "Cookie.matchValue", "matchValue" -> CookieOp.MatchValue
            "Cookie.countCookies", "countCookies" -> CookieOp.CountCookies
            "Cookie.valueHasOnlyAlphaNumericChar", "valueHasOnlyAlphaNumericChar" -> CookieOp.ValueHasOnlyAlphaNumericChar
            "Cookie.valueLengthEquals", "valueLengthEquals" -> CookieOp.ValueLengthEquals
            else -> throw InvalidConditionException("Unknown cookie operator: $value")
        }

        private fun headerOpFromString(value: String): HeaderOp = when (value) {
            "Header.hasHeader", "hasHeader" -> HeaderOp.HasHeader
            "Header.headerHasValue", "headerHasValue" -> HeaderOp.HeaderHasValue
            "Header.matchHeader", "matchHeader" -> HeaderOp.MatchHeader
            "Header.matchHeaderValue", "matchHeaderValue" -> HeaderOp.MatchHeaderValue
            "Header.isHeaderRepeated", "isHeaderRepeated" -> HeaderOp.IsHeaderRepeated
            "Header.headerHasSpecialChar", "headerHasSpecialChar" -> HeaderOp.HeaderHasSpecialChar
            "Header.totalHeadersCountEquals", "totalHeadersCountEquals" -> HeaderOp.TotalHeadersCountEquals
            "Header.headerCountEquals", "headerCountEquals" -> HeaderOp.HeaderCountEquals
            "Header.matchAllHeadersValue", "matchAllHeadersValue" -> HeaderOp.MatchAllHeadersValue
            else -> throw InvalidConditionException("Unknown header operator: $value")
        }

        private fun requestOpFromString(value: String): RequestOp = when (value) {
            "Request.methodEquals", "methodEquals" -> RequestOp.MethodEquals
            "Request.methodNotEquals", "methodNotEquals" -> RequestOp.MethodNotEquals
            "Request.isGetRequest", "isGetRequest" -> RequestOp.IsGetRequest
            "Request.isPostRequest", "isPostRequest" -> RequestOp.IsPostRequest
            "Request.isHeadRequest", "isHeadRequest" -> RequestOp.IsHeadRequest
            "Request.isPutRequest", "isPutRequest" -> RequestOp.IsPutRequest
            "Request.isDeleteRequest", "isDeleteRequest" -> RequestOp.IsDeleteRequest
            "Request.isConnectRequest", "isConnectRequest" -> RequestOp.IsConnectRequest
            "Request.isOptionsRequest", "isOptionsRequest" -> RequestOp.IsOptionsRequest
            "Request.isTraceRequest", "isTraceRequest" -> RequestOp.IsTraceRequest
            "Request.isPatchRequest", "isPatchRequest" -> RequestOp.IsPatchRequest
            "Request.queryStringEquals", "queryStringEquals" -> RequestOp.QueryStringEquals
            "Request.queryStringNotEquals", "queryStringNotEquals" -> RequestOp.QueryStringNotEquals
            "Request.queryStringContains", "queryStringContains" -> RequestOp.QueryStringContains
            "Request.queryStringNotContains", "queryStringNotContains" -> RequestOp.QueryStringNotContains
            "Request.queryStringMatch", "queryStringMatch" -> RequestOp.QueryStringMatch
            "Request.queryStringNotMatch", "queryStringNotMatch" -> RequestOp.QueryStringNotMatch
            "Request.uriEquals", "uriEquals" -> RequestOp.UriEquals
            "Request.uriNotEquals", "uriNotEquals" -> RequestOp.UriNotEquals
            "Request.uriContains", "uriContains" -> RequestOp.UriContains
            "Request.uriNotContains", "uriNotContains" -> RequestOp.UriNotContains
            "Request.uriMatch", "uriMatch" -> RequestOp.UriMatch
            "Request.uriNotMatch", "uriNotMatch" -> RequestOp.UriNotMatch
            "Request.schemeEquals", "schemeEquals" -> RequestOp.SchemeEquals
            "Request.schemeNotEquals", "schemeNotEquals" -> RequestOp.SchemeNotEquals
            "Request.protocolEquals", "protocolEquals" -> RequestOp.ProtocolEquals
            "Request.protocolNotEquals", "protocolNotEquals" -> RequestOp.ProtocolNotEquals
            "Request.protocolMatch", "protocolMatch" -> RequestOp.ProtocolMatch
            "Request.protocolNotMatch", "protocolNotMatch" -> RequestOp.ProtocolNotMatch
            "Request.urlEquals", "urlEquals" -> RequestOp.UrlEquals
            "Request.urlNotEquals", "urlNotEquals" -> RequestOp.UrlNotEquals
            "Request.urlMatch", "urlMatch" -> RequestOp.UrlMatch
            "Request.urlNotMatch", "urlNotMatch" -> RequestOp.UrlNotMatch
            "Request.urlContains", "urlContains" -> RequestOp.UrlContains
            "Request.urlNotContains", "urlNotContains" -> RequestOp.UrlNotContains
            "Request.serverEquals", "serverEquals" -> RequestOp.ServerEquals
            "Request.serverNotEquals", "serverNotEquals" -> RequestOp.ServerNotEquals
            "Request.serverMatch", "serverMatch" -> RequestOp.ServerMatch
            "Request.serverNotMatch", "serverNotMatch" -> RequestOp.ServerNotMatch
            "Request.portEquals", "portEquals" -> RequestOp.PortEquals
            "Request.portNotEquals", "portNotEquals" -> RequestOp.PortNotEquals
            else -> throw InvalidConditionException("Unknown request operator: $value")
        }
    }
}
