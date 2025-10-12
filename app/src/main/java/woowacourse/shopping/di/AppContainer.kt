package woowacourse.shopping.di

import kotlin.reflect.KClass

interface AppContainer {
    fun <T : Any> get(kClass: KClass<T>): T

    fun <T : Any> canResolve(clazz: KClass<T>): Boolean
}
