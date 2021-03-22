package com.example.custom_detekt_rules.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtImportDirective

class NoInternalImportRule(config: Config = Config.empty) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName, Severity.Maintainability,
        "Don't import from an internal package as they are subject to change.",
        Debt(1, 2, 4)
    )

    override fun visitImportDirective(importDirective: KtImportDirective) {
        val import = importDirective.importPath?.pathStr

        if (import?.contains("internal") == true) {
            report(
                CodeSmell(
                    issue, Entity.from(importDirective),
                    "Importing '$import' which is an internal import."
                )
            )
        }
    }
}
