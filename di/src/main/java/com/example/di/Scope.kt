package com.example.di

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.hasAnnotation

sealed class Scope {
    object Application : Scope()

    object Activity : Scope()

    object ViewModel : Scope()

    companion object {
        fun from(element: KAnnotatedElement): Scope {
            val lifespans: List<Annotation> =
                element.annotations.filter { annotation: Annotation ->
                    annotation.annotationClass.hasAnnotation<Lifespan>()
                }
            if (lifespans.size > 1) error("$element has more than one lifespan: $lifespans")

            val lifespan: Annotation? = lifespans.firstOrNull()
            return when (lifespan) {
                is ViewModelLifespan -> ViewModel
                is ActivityLifespan -> Activity
                else -> Application
            }
        }
    }
}
