package com.example.di

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.hasAnnotation

enum class Scope {
    APPLICATION,
    VIEWMODEL,
    ACTIVITY,
    ;

    companion object {
        fun from(element: KAnnotatedElement): Scope {
            val lifespans: List<Annotation> =
                element.annotations.filter { annotation: Annotation ->
                    annotation.annotationClass.hasAnnotation<Lifespan>()
                }
            if (lifespans.size > 1) error("$element has more than one lifespan: $lifespans")

            val lifespan: Annotation? = lifespans.firstOrNull()
            return when (lifespan) {
                is ViewModelLifespan -> VIEWMODEL
                is ActivityLifespan -> ACTIVITY
                else -> APPLICATION
            }
        }
    }
}
