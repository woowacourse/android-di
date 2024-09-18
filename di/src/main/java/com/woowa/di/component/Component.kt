package com.woowa.di.component

import kotlin.reflect.KClass

interface Component {
    val parent: KClass<out Component>?

    fun getDIInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any?
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class InstallIn(val component: KClass<out Component>)
