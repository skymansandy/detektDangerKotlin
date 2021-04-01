package com.example.custom_detekt_rules.rule

import com.example.custom_detekt_rules.util.PSIUtil.getKotlinType
import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*

class UseGCAssistSubscribeRule : Rule() {

    override val issue = Issue(
            javaClass.simpleName,
            Severity.Warning,
            "UseGCAssistSubscribeRule",
            Debt(1)
    )

    override fun visitReferenceExpression(expression: KtReferenceExpression) {
        super.visitReferenceExpression(expression)
        if (expression.text.contains("subscribe")) {
            print("\nCalled subscribe: ${expression.text}")
            if (isCalledOnTypeObservable(expression)) {
                report(
                        CodeSmell(
                                issue,
                                Entity.from(expression),
                                "Use AGTC subscribe instead of subscribe at `${expression.name}`"
                        )
                )
            }
        }
    }


    private fun isCalledOnTypeObservable(expression: KtReferenceExpression): Boolean {
        val dotExpression = expression.prevSibling
        val callerObj = when (dotExpression?.parent) {
            is KtDotQualifiedExpression -> dotExpression.prevSibling
            else -> return false
        }
        print("\n==> Caller obj: ${callerObj.text} Type: ${callerObj?.getKotlinType(bindingContext)}")
        return callerObj?.getKotlinType(bindingContext)?.startsWith("snail.observable") ?: false
    }
}