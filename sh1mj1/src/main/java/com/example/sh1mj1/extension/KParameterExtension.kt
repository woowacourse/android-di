package com.example.sh1mj1.extension

import kotlin.reflect.KClass
import kotlin.reflect.KParameter

fun KParameter.typeToKClass(): KClass<*> = this.type.classifier as KClass<*>
