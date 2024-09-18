package com.woowa.di.component

import kotlin.reflect.KClass

interface ComponentManager {

    fun getDIInstanceOrNull(key: KClass<*>, qualifier: KClass<out Annotation>?): Any?

    fun getBinderType(key: KClass<*>): KClass<*>

    fun getBinderTypeOrNull(key: KClass<*>): KClass<*>?

    fun <binder : Any> registerBinder(binderClazz: KClass<binder>)
}

