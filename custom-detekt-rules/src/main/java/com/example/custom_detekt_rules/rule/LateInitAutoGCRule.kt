package com.example.custom_detekt_rules.rule

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*

class LateInitAutoGCRule : Rule() {

    override val issue = Issue(
            javaClass.simpleName,
            Severity.Warning,
            "Lateinit not marked with @AutoGC",
            Debt(1)
    )

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)
        val modifierList = property.modifierList
        val annotationList = modifierList?.annotationEntries
        if (ifHasLateinitModifier(modifierList) && !hasAutoGCAnnotation(annotationList)) {
            report(
                    CodeSmell(
                            issue,
                            Entity.from(property),
                            "Lateinit variable `${property.name}` not marked with @AutoGC."
                    )
            )
        }
    }

    private fun hasAutoGCAnnotation(annotationList: List<KtAnnotationEntry>?): Boolean {
        // Ends with? / contains? / equals? (Since annotations can be declared in many ways.
        // @AutoGC, @com.package.class.AutoGC, @package.class.AutoGC etc
        return annotationList?.any { it.text.endsWith("AutoGC") } == true
    }

    private fun ifHasLateinitModifier(modifierList: KtModifierList?): Boolean {
        return modifierList?.text?.contains("lateinit") == true
    }
}