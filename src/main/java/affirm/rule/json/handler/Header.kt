package affirm.rule.json.handler

enum class Header(val value: String) {
    HasHeader("Header.hasHeader"),
    HeaderHasValue("Header.headerHasValue"),
    MatchHeader("Header.matchHeader"),
    MatchHeaderValue("Header.matchHeaderValue"),
    IsHeaderRepeated("Header.isHeaderRepeated"),
    HeaderHasSpecialChar("Header.headerHasSpecialChar"),
    TotalHeadersCountEquals("Header.totalHeadersCountEquals"),
    HeaderCountEquals("Header.headerCountEquals"),
    MatchAllHeadersValue("Header.matchAllHeadersValue")
}