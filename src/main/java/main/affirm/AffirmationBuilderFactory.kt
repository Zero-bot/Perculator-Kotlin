package main.affirm

import main.affirm.rule.Rule
import main.result.Action
import javax.servlet.http.HttpServletRequest

class AffirmationBuilderFactory(private val httpServletRequest: HttpServletRequest, private val rules: Array<Rule>) {

    fun affirm():Action{
        var action: Action = Action.Allow
        for(rule in rules){
            action = rule.evaluate(httpServletRequest)
            if(action == Action.Reject) return Action.Reject
        }
        return action
    }

}