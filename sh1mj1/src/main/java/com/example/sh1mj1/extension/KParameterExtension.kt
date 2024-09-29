package com.example.sh1mj1.extension

import com.example.sh1mj1.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation

fun KParameter.typeToKClass(): KClass<*> = this.type.classifier as KClass<*>

fun KFunction<Any>.injectableProperties(): List<KParameter> = this.parameters.filter { kParameter ->
    kParameter.hasAnnotation<Inject>()
}