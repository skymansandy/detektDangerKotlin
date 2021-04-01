package com.example.custom_detekt_rules.rule

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*

class FragmentDefaultConstructorRule : Rule() {

    override val issue = Issue(
        javaClass.simpleName,
        Severity.Defect,
        "Fragment has overridden default constructor. " +
                "In Android this can lead to unexpected behaviour. See more at https://stackoverflow.com/questions/12062946/why-do-i-want-to-avoid-non-default-constructors-in-fragments",
        Debt(1)
    )

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        if (isFragmentClass(classOrObject) && hasOverriddenDefaultConstructor(classOrObject)) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(classOrObject),
                    "Fragment class `${classOrObject.name}` has overridden primary constructor. This should be avoided in Android."
                )
            )
        }
    }

    private fun isFragmentClass(classOrObject: KtClassOrObject): Boolean {
        for (superEntry in classOrObject.superTypeListEntries) {
            when (superEntry.text) {
                "Fragment()" -> {
                    return true
                }
            }
        }
        return false
    }

    private fun hasOverriddenDefaultConstructor(classOrObject: KtClassOrObject): Boolean {
        return classOrObject.primaryConstructor?.valueParameters?.size ?: 0 > 0
    }
}