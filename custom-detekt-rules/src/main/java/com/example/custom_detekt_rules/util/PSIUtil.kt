package com.example.custom_detekt_rules.util

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.typeBinding.createTypeBinding
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.supertypes

object PSIUtil {

    fun PsiElement.getKotlinType(bindingContext: BindingContext): String? {
        return (this as? KtElement).getResolvedCall(bindingContext)
            ?.resultingDescriptor
            ?.returnType?.toString()
    }

    fun PsiElement.getKotlinJetType(bindingContext: BindingContext): KotlinType? {
        return (this as? KtElement).getResolvedCall(bindingContext)
            ?.resultingDescriptor
            ?.returnType
    }

    fun KtTypeReference.getKotlinType(bindingContext: BindingContext): String? {
        val type = createTypeBinding(bindingContext)?.type
        return type?.getJetTypeFqName(false)
    }

    fun KtTypeReference.isTypeSubtypeOf(
        superType: String,
        bindingContext: BindingContext
    ): Boolean {
        val type = createTypeBinding(bindingContext)?.type
        type?.supertypes()?.forEach {
            print(it.toString())
            if (it.toString().contains(superType)) return true
        }
        return false
    }
}