package com.daedan.di.qualifier

import com.daedan.di.annotation.Component
import kotlin.reflect.KClass

data class AnnotationQualifier(
    val annotation: KClass<out Annotation>,
) : Qualifier {
    init {
        require(
            annotation.annotations.any {
                it.annotationClass == Component::class
            },
        ) { "@Component 어노테이션으로 등록되지 않았습니다 : $annotation" }
    }
}
