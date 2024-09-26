package com.woowa.di.component

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

interface Component {
    fun getDIInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any?

    fun registerDIInstance(
        binder: Any,
        kFunc: KFunction<*>,
    )

    fun deleteAllDIInstance()
}
