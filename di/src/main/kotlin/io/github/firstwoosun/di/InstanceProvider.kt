package io.github.firstwoosun.di

import kotlin.reflect.KClass

fun interface InstanceProvider {
    fun getInstanceOrNull(type: KClass<*>): Any?
}
