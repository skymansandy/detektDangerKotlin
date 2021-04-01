package com.example.custom_detekt_rules.rule

import com.example.custom_detekt_rules.util.PSIUtil.getKotlinType
import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.*

class UncheckedTypeCastRule : Rule() {

    override val issue = Issue(
        javaClass.simpleName,
        Severity.Warning,
        "Unchecked type cast detected.",
        Debt(1)
    )

    override fun visitTypeReference(typeReference: KtTypeReference) {
        super.visitTypeReference(typeReference)

        val whiteSpace = typeReference.prevSibling as? PsiWhiteSpace ?: return
        val opReference = whiteSpace.prevSibling as? KtOperationReferenceExpression ?: return
        val psiElement = opReference.prevSibling?.prevSibling ?: return
        print("Before operation expr: ${psiElement.text} and its type: ${psiElement.getKotlinType(bindingContext)}")
        if (typeReference.isApiV3() && isUnsafeAsCall(opReference)) {
            print("\n\nReference Expression: ${typeReference.text}")
            reportWarning(typeReference)
        } else {
            checkRecursive(typeReference)
        }
    }

    /**
     * Checks in all children type references
     */
    private fun checkRecursive(typeReference: KtTypeReference) {
        val userTypes = typeReference.children
        userTypes.forEach { userType ->
            val userTypeItem = if (userType is KtNullableType) userType.firstChild else userType
            val typeArgList = (userTypeItem as? KtUserType)?.lastChild as? KtTypeArgumentList
            typeArgList?.children?.forEach { projection ->
                val typeProjection = projection as? KtTypeProjection
                typeProjection?.children?.forEach { typeRef ->
                    val typeRefItem = typeRef as? KtTypeReference
                    typeRefItem?.checkApiV3() ?: return@forEach
                    checkRecursive(typeRefItem)
                }
            }
        }
    }

    private fun KtTypeReference.checkApiV3() {
        if (isApiV3() && !text.endsWith("?")) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(this),
                    "Expression `${text}` is an unchecked. Add ? to end of the type for null safety!"
                )
            )
        }
    }

    private fun KtTypeReference.isApiV3(): Boolean {
        val type = getKotlinType(bindingContext)
        print("\nExpr : $text\tType: $type")
        return type?.startsWith("com.example.detekt") ?: false
    }

    private fun isUnsafeAsCall(opReference: KtOperationReferenceExpression): Boolean {
        return opReference.firstChild?.text?.equals("as?") == false
    }

    private fun reportWarning(typeReference: KtTypeReference) {
        report(
            CodeSmell(
                issue,
                Entity.from(typeReference),
                "Expression `${typeReference.text}` is an unchecked type cast."
            )
        )
    }
}
