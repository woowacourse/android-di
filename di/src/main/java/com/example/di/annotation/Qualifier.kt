package com.example.di.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY)
annotation class Qualifier(val type: KClass<*>)
