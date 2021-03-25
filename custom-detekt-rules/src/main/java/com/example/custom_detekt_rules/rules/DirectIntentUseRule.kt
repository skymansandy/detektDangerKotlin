package com.example.custom_detekt_rules.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.containingClass
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall

class DirectIntentUseRule : Rule() {

    override val issue = Issue(
        javaClass.simpleName,
        Severity.Warning,
        "Intent extras accessor method called directly on getIntent(). " +
                "Better to call through IntentAware interface",
        Debt.TWENTY_MINS
    )

    companion object {
        private const val allowedClassName = "IntentAware"
        private const val intentKotlinType =
            "(android.content.Intent" //Type resolved by the detekt library for an Intent instance.

        private val intentMethods = arrayOf(
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
            "getStringExtra",
            "getExtras", //call expressions till here

            "extras" //Handled reference expression 'extras' which is similar to call expression 'getExtras()'
        )
    }

    /**
     * Visits each reference expression in a file and finds out whether the expression
     * is a direct accessor of intent extras.
     */
    override fun visitReferenceExpression(expression: KtReferenceExpression) {
        super.visitReferenceExpression(expression)
        if (isExpressionInAllowedClass(expression)) return

        if (isCalledOnTypeIntent(expression) && isExtrasAccessorMethod(expression)) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(expression),
                    "Expression `${expression.text}` is a direct use of intent extras accessor. " +
                            "Consider accessing through `IntentAware` interface to handle possible null values."
                )
            )
        }
    }

    /**
     * Finds out if the passed expression is called on an instance of type 'android.content.Intent'
     */
    private fun isCalledOnTypeIntent(expression: KtExpression): Boolean {
        val dotExpression = expression.prevSibling
        val callerObj = when (dotExpression?.parent) {
            is KtDotQualifiedExpression -> dotExpression.prevSibling
            else -> return false
        }
        return (callerObj as? KtElement).getResolvedCall(bindingContext)
            ?.resultingDescriptor
            ?.returnType?.toString()
            ?.startsWith(intentKotlinType) ?: false
    }

    /**
     * Finds out if the method/accessor called on the Intent object is one of the
     * Intent extras accessor method (defined in the array `intentMethods`)
     *
     * accessing directly can happen in two ways.
     * 1. Call expr: `intent.getExtras()`
     * 2. Reference expr: `intent.extras`
     */
    private fun isExtrasAccessorMethod(expression: KtExpression): Boolean {
        when (expression) {
            is KtCallExpression -> {
                val children = expression.children
                if (children.isNotEmpty() && children[0] is KtReferenceExpression)
                    return children[0].text in intentMethods
            }
            is KtReferenceExpression -> return expression.text in intentMethods
        }
        return false
    }

    /**
     * Check if direct access is in the allowed class. In our case 'IntentAware'.
     * If so, return
     */
    private fun isExpressionInAllowedClass(expression: KtExpression): Boolean {
        return expression.containingClass()?.text?.contains(allowedClassName) == true
    }
}

