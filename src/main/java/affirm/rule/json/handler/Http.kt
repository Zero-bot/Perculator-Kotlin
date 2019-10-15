package affirm.rule.json.handler

enum class Http(val value: String) {
    Parameter("Http.Parameter"),
    Cookie("Http.Cookie"),
    Request("Http.Request"),
    Header("Http.Header")
}