package com.example.custom_detekt_rules.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.js.descriptorUtils.nameIfStandardType
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.containingClass
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.supertypes

/**
 * Reports unnecessary safe call operators (`?.`) that can be removed by the user.
 *
 * <noncompliant>
 * val a: String = ""
 * val b = a?.length
 * </noncompliant>
 *
 * <compliant>
 * val a: String? = null
 * val b = a?.length
 * </compliant>
 *
 * @active since v1.16.0
 * @requiresTypeResolution
 */
class DirectIntentUseRule : Rule() {

    override val issue = Issue(javaClass.simpleName, Severity.CodeSmell, "", Debt.FIVE_MINS)

    private val intentMethodsArray = arrayOf(
        "getBundleExtra",
        "getBooleanArrayExtra",
        "getBooleanExtra",
        "getByteArrayExtra",
        "getByteExtra",
        "getCharArrayExtra",
        "getCharExtra",
        "getCharSequenceArrayExtra",
        "getCharSequenceArrayListExtra",
        "getCharSequenceExtra",
        "getDoubleArrayExtra",
        "getDoubleExtra",
        "getFloatArrayExtra",
        "getFloatExtra",
        "getIntArrayExtra",
        "getIntExtra",
        "getIntegerArrayListExtra",
        "getLongArrayExtra",
        "getLongExtra",
        "getParcelableArrayExtra",
        "getParcelableArrayListExtra",
        "getParcelableExtra",
        "getSerializableExtra",
        "getShortArrayExtra",
        "getShortExtra",
        "getStringArrayExtra",
        "getStringArrayListExtra",
        "getStringExtra"
    )

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)
        if (expression.context is KtDotQualifiedExpression && expression.calleeExpression is KtNameReferenceExpression) {
            val children = expression.children
            if (children.isNotEmpty() && children[0] is KtReferenceExpression) {
                if (children[0].text in intentMethodsArray) {
                    val isIntent = isIntentMethod(expression)
                    print("IsIntent: $isIntent")
                    if (isIntent)
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

    private fun isIntentMethod(expression: KtCallExpression): Boolean {
        val dotExpression = expression.prevSibling
        val caller = when (dotExpression?.parent) {
            is KtDotQualifiedExpression -> dotExpression.prevSibling
            else -> return false
        }
        return (caller as? KtElement).getResolvedCall(bindingContext)
            ?.resultingDescriptor
            ?.returnType?.toString()
            ?.startsWith("(android.content.Intent") ?: false
    }

    //TODO handle intent.extras call
}

