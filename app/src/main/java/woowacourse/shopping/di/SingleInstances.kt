package woowacourse.shopping.di

import kotlin.reflect.KClass

data class SingleInstances(val values: List<SingleInstance<out Any>>) {

    fun find(kClass: KClass<*>): Any? {
        return values.find { it.isTypeOf(kClass) }?.instance
    }
}
