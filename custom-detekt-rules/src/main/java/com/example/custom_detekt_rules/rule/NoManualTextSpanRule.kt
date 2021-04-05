package com.example.custom_detekt_rules.rule

import com.example.custom_detekt_rules.util.PSIUtil.getKotlinJetType
import com.example.custom_detekt_rules.util.PSIUtil.getKotlinType
import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.*

class NoManualTextSpanRule : Rule() {

    private val filesToExclude = arrayOf("MainActivity.kt")

    override val issue = Issue(
        javaClass.simpleName,
        Severity.Warning,
        "NoManualTextSpanRule",
        Debt(1)
    )

    override fun visitReferenceExpression(expression: KtReferenceExpression) {
        super.visitReferenceExpression(expression)
        if (expression.text.contains("setSpan")) {
            if (!isInExcludedFiles(expression) && isCalledOnTypeSpannableString(expression)) {
                report(
                    CodeSmell(
                        issue,
                        Entity.from(expression),
                        "Looks like `${expression.name}` is a manual spanning. Prefer spanning texts using DSL `buildSpannedString`."
                    )
                )
            }
        }
    }

    private fun isInExcludedFiles(expression: KtReferenceExpression): Boolean {
        return filesToExclude.any { expression.containingFile.name.endsWith(it) }
    }


    private fun isCalledOnTypeSpannableString(expression: KtReferenceExpression): Boolean {
        val dotExpression = expression.prevSibling
        val callerObj = when (dotExpression?.parent) {
            is KtDotQualifiedExpression -> dotExpression.prevSibling
            else -> return false
        }
        print(
            "\n==> Caller obj: ${callerObj.text} Type: ${
                callerObj?.getKotlinJetType(
                    bindingContext
                )?.getJetTypeFqName(false)
            }"
        )
        return callerObj?.getKotlinType(bindingContext) == "android.text.SpannableString"
    }
}