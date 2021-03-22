package com.example.custom_detekt_rules.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.KtNamedFunction

class TooManyFunctionsRule : Rule() {

    override val issue = Issue(javaClass.simpleName, Severity.CodeSmell, "", Debt.FIVE_MINS)

    private var amount: Int = 0

    override fun visitFile(file: PsiFile) {
        super.visitFile(file)
        if (amount > 4) {
            report(CodeSmell(issue, Entity.from(file), "Code smell bro"))
        }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        amount++
    }
}