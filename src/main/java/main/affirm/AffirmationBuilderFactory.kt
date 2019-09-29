package main.affirm

import main.result.ActionResult
import javax.servlet.http.HttpServletRequest

class AffirmationBuilderFactory(val httpServletRequest: HttpServletRequest) {

    fun affirm():ActionResult{
        return ActionResult.Allow
    }

}