package com.example.sh1mj1.extension

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

fun KProperty1<*, *>.typeToKClass(): KClass<*> = this.returnType.classifier as KClass<*>