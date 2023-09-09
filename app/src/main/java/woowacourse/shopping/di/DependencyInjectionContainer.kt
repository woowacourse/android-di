package woowacourse.shopping.di

import kotlin.reflect.KClass

class DependencyInjectionContainer(
    private val values: List<Dependency<out Any>> = listOf()
) : Container {

    override fun find(kClass: KClass<*>): Any? = values.find { it.isTypeOf(kClass) }?.instance
}
