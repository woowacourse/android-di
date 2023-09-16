package com.woowacourse.bunadi.injector

import kotlin.reflect.KClass

interface Injector {
    fun <T : Any> inject(clazz: KClass<T>): T
    fun <T : Any> injectMemberProperties(clazz: KClass<T>, instance: Any)
    fun caching(dependencyKey: DependencyKey, dependency: Any? = null)
    fun clear()
}
