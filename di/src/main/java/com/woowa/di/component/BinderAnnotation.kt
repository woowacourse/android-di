package com.woowa.di.component

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class InstallIn(val component: KClass<out Component>)