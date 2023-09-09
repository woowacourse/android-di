package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

class DependencyInjectionContainer(
    private val values: List<Dependency<out Any>> = listOf()
) : Container {

    override fun find(clazz: KClass<*>): Any? =
        values.find { it.isTypeOf(clazz) }?.instance

    override fun find(clazzName: String): Any? =
        values.find { it.instance::class.simpleName == clazzName }?.instance
}
