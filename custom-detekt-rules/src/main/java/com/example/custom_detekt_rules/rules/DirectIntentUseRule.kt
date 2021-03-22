package com.example.custom_detekt_rules.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction

class DirectIntentUseRule : Rule() {

    override val issue = Issue(javaClass.simpleName, Severity.CodeSmell, "", Debt.FIVE_MINS)

    private var isIntentAwareFile = false

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        isIntentAwareFile = true
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        if (isIntentAwareFile) return

        val statements = function.bodyBlockExpression?.statements ?: arrayListOf()
        statements.forEach {
            val statement = it
            if (it.text.contains("getStringExtra")) {
                report(
                    CodeSmell(
                        issue, Entity.from(function),
                        "Function '$function' has a direct use of intent. Caution!!"
                    )
                )
            }
        }
    }
}