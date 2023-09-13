package com.hyegyeong.di

import kotlin.reflect.KClass

interface Container {
    val instances: MutableMap<AnnotationType, Any>

    fun addInstance(instance: Any)

    fun getInstance(annotationType: AnnotationType): Any?
    fun hasDuplicateObjectsOfType(kClass: KClass<*>): Boolean
}