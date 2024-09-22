package com.woowa.di.component

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

interface Component2 {
    fun getDIInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any?

    fun registerDIInstance(
        binder:Any,
        kFunc:KFunction<*>,
    )


    fun deleteDIInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    )
}