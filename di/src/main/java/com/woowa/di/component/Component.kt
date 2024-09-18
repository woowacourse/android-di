package com.woowa.di.component

import kotlin.reflect.KClass

interface Component {
    fun getDIInstanceOrNull(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any?
}

