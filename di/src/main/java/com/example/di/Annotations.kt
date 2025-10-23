package com.example.di

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.hasAnnotation

@Target(AnnotationTarget.PROPERTY)
annotation class Inject

@Target(AnnotationTarget.FUNCTION)
annotation class Dependency

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier {
    companion object {
        fun from(element: KAnnotatedElement): Annotation? {
            val qualifiers: List<Annotation> =
                element.annotations.filter { annotation: Annotation ->
                    annotation.annotationClass.hasAnnotation<Qualifier>()
                }
            if (qualifiers.size > 1) error("$element has more than one qualifier: $qualifiers")
            return qualifiers.firstOrNull()
        }
    }
}

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Lifespan {
    companion object {
        fun from(element: KAnnotatedElement): Annotation? {
            val lifespans: List<Annotation> =
                element.annotations.filter { annotation: Annotation ->
                    annotation.annotationClass.hasAnnotation<Lifespan>()
                }
            if (lifespans.size > 1) error("$element has more than one lifespan: $lifespans")
            return lifespans.firstOrNull()
        }
    }
}

@Target(AnnotationTarget.FUNCTION)
@Lifespan
annotation class ApplicationLifespan

@Target(AnnotationTarget.FUNCTION)
@Lifespan
annotation class ViewModelLifespan

@Target(AnnotationTarget.FUNCTION)
@Lifespan
annotation class ActivityLifespan
