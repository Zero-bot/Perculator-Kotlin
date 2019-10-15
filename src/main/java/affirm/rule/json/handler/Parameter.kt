package affirm.rule.json.handler

enum class Parameter(val value: String) {
    HasParameter("Parameter.hasParameter"),
    MatchParameter("Parameter.matchParameter"),
    HasParameterWithValue("Parameter.hasParameterWithValue"),
    MatchParameterWithValue("Parameter.matchParameterWithValue"),
    Match("Parameter.match"),
    CountParameters("Parameter.countParameters"),
    ValueHasOnlyAlphaNumericChar("Parameter.valueHasOnlyAlphaNumericChar")
}