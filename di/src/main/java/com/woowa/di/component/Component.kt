package com.woowa.di.component

import kotlin.reflect.KClass

interface Component {
    fun getDIInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any?
}
