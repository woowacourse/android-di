package woowacourse.shopping.di

import kotlin.reflect.KClass

class DependencyInjectionContainer(private val values: List<Dependency<out Any>>) {

    fun find(kClass: KClass<*>, onFailure: () -> Any = { }): Any =
        values
            .find { it.isTypeOf(kClass) }
            ?.instance
            ?: onFailure.invoke()
}
