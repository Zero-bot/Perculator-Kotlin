package affirm


import affirm.rule.Rule
import http.wrapper.RequestWrapper
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import result.Action
import serializer.JsonRuleSet
import java.io.File
import java.io.FileNotFoundException
import javax.servlet.http.HttpServletRequest

class Perculator(private val jsonRulesPath: String) {

    private val json = Json(JsonConfiguration.Stable)
    private var jsonRulesFileLastModified: Long = 0
    var ruleSet: Map<String, Rule>
    get() {
        if(isJsonRulesFileModified(File(jsonRulesPath))){
            field = json.parse(JsonRuleSet.serializer(), File(jsonRulesPath).readText()).toRulesMap()
            return field
        }
        return field
    }

    init {
        val jsonRulesFile = File(jsonRulesPath)
        if(!jsonRulesFile.exists()) throw FileNotFoundException(jsonRulesPath)
        jsonRulesFileLastModified = jsonRulesFile.lastModified()
        ruleSet = json.parse(JsonRuleSet.serializer(), jsonRulesFile.readText()).toRulesMap()
    }

    private fun isJsonRulesFileModified(jsonRulesFile: File) = jsonRulesFile.lastModified() != jsonRulesFileLastModified

    fun evaluate(httpServletRequest: RequestWrapper): Action {
        return AffirmationBuilderFactory(httpServletRequest, this.ruleSet).affirm()
    }

}