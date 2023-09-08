package woowacourse.shopping.di

import kotlin.reflect.KClass

class Dependencies(private val values: List<Dependency<out Any>>) {

    fun find(kClass: KClass<*>): Any? {
        return values.find { it.isTypeOf(kClass) }?.instance
    }
}
