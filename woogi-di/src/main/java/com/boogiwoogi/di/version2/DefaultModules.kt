package com.boogiwoogi.di.version2

import com.boogiwoogi.di.Provides
import com.boogiwoogi.di.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

open class DefaultModules : Modules {

    override fun provideInstanceOf(clazz: KClass<*>): Any? {
        val functions = this::class
            .functions
            .filter { it.hasAnnotation<Provides>() }
            .firstOrNull { it.returnType.jvmErasure == clazz }

        return functions?.call(this)
    }

    override fun provideInstanceOf(simpleName: String): Any? {
        val function = this::class
            .functions
            .filter { it.hasAnnotation<Qualifier>() }
            .firstOrNull { it.findAnnotation<Qualifier>()!!.simpleName == simpleName }

        return function?.call(this)
    }
}
