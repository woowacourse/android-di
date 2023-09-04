package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

data class SingleInstance<T : Any>(
    val instance: T,
) {

    private val classes = instance::class.superclasses + instance::class

    val isTypeOf: (kClass: KClass<*>) -> (Boolean) = { kClass ->
        classes.contains(kClass)
    }
}
