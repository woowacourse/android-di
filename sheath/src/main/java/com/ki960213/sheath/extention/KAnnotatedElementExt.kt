package com.ki960213.sheath.extention

import com.ki960213.sheath.annotation.Qualifier
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

val KAnnotatedElement.customQualifiedName: String?
    get() {
        val qualifiedName = this.findAnnotation<Qualifier>()?.value
        if (qualifiedName != null) return qualifiedName

        val annotationAttachedQualifier = this.annotations
            .find { it.annotationClass.hasAnnotation<Qualifier>() } ?: return null

        return annotationAttachedQualifier.annotationClass
            .findAnnotation<Qualifier>()
            ?.value
    }
