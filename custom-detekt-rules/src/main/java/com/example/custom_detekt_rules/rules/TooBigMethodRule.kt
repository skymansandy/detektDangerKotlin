package com.example.custom_detekt_rules.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtNamedFunction

class TooBigMethodRule(config: Config = Config.empty) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName, Severity.Maintainability,
        "lot of LOC in the method",
        Debt(1)
    )

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        if (function.bodyBlockExpression?.statements?.size ?: 0 > 5) {
            report(
                CodeSmell(
                    issue, Entity.from(function),
                    "Function '$function' has more than 5 lines"
                )
            )
        }
    }
}
