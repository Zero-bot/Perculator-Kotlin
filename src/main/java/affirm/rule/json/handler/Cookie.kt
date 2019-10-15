package affirm.rule.json.handler

enum class Cookie(val value: String) {
    HasCookie("Cookie.hasCookie"),
    CookieHasValue("Cookie.cookieHasValue"),
    MatchCookie("Cookie.matchCookie"),
    MatchValue("Cookie.matchValue"),
    CountCookies("Cookie.countCookies"),
    ValueHasOnlyAlphaNumericChar("Cookie.valueHasOnlyAlphaNumericChar"),
    ValueLengthEquals("Cookie.valueLengthEquals")
}