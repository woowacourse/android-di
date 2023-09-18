package com.ki960213.sheath.annotation

import kotlin.reflect.KClass

@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
)
annotation class Qualifier(val value: KClass<*>)
