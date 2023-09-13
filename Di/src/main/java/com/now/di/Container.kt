package com.now.di

import com.now.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

object Container {
    private val dependency = mutableMapOf<DependencyType, Any>()

    private val nonAnnotationMap = mutableMapOf<KClass<*>, Any>()

    private val annotationMap = mutableMapOf<String, Any>()

    const val defaultQualifier = ""

    fun getInstance(dependencyType: DependencyType): Any? {
        return dependency[dependencyType]
    }

    fun getInstance(type: KClass<*>): Any? {
        return nonAnnotationMap[type]
    }

    fun addInstance(type: KClass<*>, instance: Any) {
        // 어노테이션이 있으면 Annotation맵에 저장
        if (instance::class.hasAnnotation<Qualifier>()) {
            val qualifier = instance::class.findAnnotation<Qualifier>()

            // getInstance를 가능하게 하기 위해 Qualifier가 있는 경우 Default 값 저장
            nonAnnotationMap[type] = defaultQualifier
        } else {
            // 어노테이션이 없으면 nonAnnotationMap에 저장
            nonAnnotationMap[type] = instance
        }
    }

    fun addInstance2(type: KClass<*>, instance: Any, annotation: Annotation?) {
        dependency[DependencyType(type, annotation)] = instance
    }

    fun clear() {
        annotationMap.clear()
        nonAnnotationMap.clear()
    }
}
