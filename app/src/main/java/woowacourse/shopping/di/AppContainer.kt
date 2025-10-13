package woowacourse.shopping.di

import kotlin.reflect.KClass

interface AppContainer {
    fun <T : Any> getInstance(clazz: KClass<T>): T

    fun injectField(instance: Any)
}
