package com.example.sh1mj1.extension

import com.example.sh1mj1.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

fun KProperty1<*, *>.typeToKClass(): KClass<*> = this.returnType.classifier as KClass<*>

fun <T : Any> injectableProperties(kClass: KClass<T>) =
    kClass.memberProperties.filter { it.hasAnnotation<Inject>() }