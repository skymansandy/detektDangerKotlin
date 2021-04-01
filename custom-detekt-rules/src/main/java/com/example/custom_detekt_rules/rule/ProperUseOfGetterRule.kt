package com.example.custom_detekt_rules.rule

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.containingClass
import kotlin.math.exp

class ProperUseOfGetterRule : Rule() {

    override val issue = Issue(javaClass.simpleName, Severity.CodeSmell, "", Debt.FIVE_MINS)

    override fun visitPropertyAccessor(accessor: KtPropertyAccessor) {
        super.visitPropertyAccessor(accessor)
        report(
            CodeSmell(
                issue, Entity.from(accessor),
                "Accessor '$accessor' not allowed"
            )
        )
    }
}