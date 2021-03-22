package com.example.custom_detekt_rules.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtPrimaryConstructor
import org.jetbrains.kotlin.psi.psiUtil.getValueParameters

class NoConstructorFragmentsRule : Rule() {

    override val issue = Issue(javaClass.simpleName, Severity.CodeSmell, "", Debt(7))

    override fun visitPrimaryConstructor(constructor: KtPrimaryConstructor) {
        super.visitPrimaryConstructor(constructor)
        if(constructor.valueParameters.size > 0){
            report(
                CodeSmell(
                    issue, Entity.from(constructor),
                    "Fragment '$constructor' should not override default constructor"
                )
            )
        }
    }
}