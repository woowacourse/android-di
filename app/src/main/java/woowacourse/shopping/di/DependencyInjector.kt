package woowacourse.shopping.di

import kotlin.reflect.KClass

object DependencyInjector {
    fun <T : Any> inject(targetClass: KClass<T>): T {
        return DIContainer.getInstance(targetClass)
    }
}
