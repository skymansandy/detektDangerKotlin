package com.example.custom_detekt_rules.rule

import com.example.custom_detekt_rules.util.PSIUtil.getKotlinType
import com.example.custom_detekt_rules.util.PSIUtil.isTypeSubtypeOf
import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.containingClass

class SingleSourceOfLaunchRule : Rule() {

    override val issue = Issue(
        javaClass.simpleName,
        Severity.Warning,
        "It's ideal to launch activities from a single source. Especially the ones taking input params. " +
                "It'll be easy to change the call site easily in case of input change.",
        Debt(1)
    )

    override fun visitClassLiteralExpression(expression: KtClassLiteralExpression) {
        super.visitClassLiteralExpression(expression)
        val referenceExpr = expression.firstChild as? KtNameReferenceExpression
        referenceExpr ?: return

    }

    override fun visitTypeProjection(typeProjection: KtTypeProjection) {
        super.visitTypeProjection(typeProjection)
        val typeReference = typeProjection.firstChild as? KtTypeReference ?: return
        val className = typeReference.text
        val type = typeReference.getKotlinType(bindingContext)
        if (typeReference.isTypeSubtypeOf(
                "Activity",
                bindingContext
            ) && typeReference.containingClass()?.name != className
        ) {
            print("\nClass type: $className, class: ${typeReference.containingClass()?.name} ")
            report(
                CodeSmell(
                    issue,
                    Entity.from(typeReference),
                    "Expression `${typeReference.text}` looks like a launch code of an activity. " +
                            "Prefer using launch method from the respective Activity class for better maintainability."
                )
            )
        }
    }
}