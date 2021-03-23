package com.example.custom_detekt_rules.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.containingClass
import kotlin.math.exp

class DirectIntentUseRule : Rule() {

    override val issue = Issue(javaClass.simpleName, Severity.CodeSmell, "", Debt.FIVE_MINS)

    override fun visitBlockExpression(expression: KtBlockExpression) {
        super.visitBlockExpression(expression)
        expression.containingClass()
        if (expression.containingClass()?.nameIdentifier?.text?.contains("MainActivity") == true) return

        expression.statements.forEach {
            val statement = it
            if (it.text.contains("getStringExtra")) {
                report(
                    CodeSmell(
                        issue, Entity.from(expression),
                        "Expression '$expression' has a direct use of intent. Caution!!"
                    )
                )
            }
        }
    }
}