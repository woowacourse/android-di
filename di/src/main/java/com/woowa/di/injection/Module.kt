package com.woowa.di.injection

import kotlin.reflect.KClass

interface Module<T : Any, type : Any> {
    fun getDIInstance(type: KClass<out type>): type

    fun getDIInstance(
        type: KClass<out type>,
        qualifier: KClass<out Annotation>,
    ): type
}
