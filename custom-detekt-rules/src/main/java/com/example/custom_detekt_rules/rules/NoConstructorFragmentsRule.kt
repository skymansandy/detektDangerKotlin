package com.example.custom_detekt_rules.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.containingClass
import org.jetbrains.kotlin.psi.psiUtil.containingClassOrObject
import org.jetbrains.kotlin.psi.psiUtil.getValueParameters
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

class NoConstructorFragmentsRule : Rule() {

    override val issue = Issue(javaClass.simpleName, Severity.CodeSmell, "", Debt(7))

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        for (superEntry in classOrObject.superTypeListEntries) {
            when (superEntry.text) {
                "Fragment()" -> {
                    if (classOrObject.primaryConstructor?.valueParameters?.size ?: 0 > 0) {
                        report(
                            classOrObject,
                            "Fragment $classOrObject has overriden primary constructor. Dont!"
                        )
                    }
                }
            }
        }
    }

    private fun report(classOrObject: KtClassOrObject, message: String) {
        report(CodeSmell(issue, Entity.from(classOrObject), message))
    }

}