package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

class Dependency<T : Any>(
    val instance: T
) {

    private val clazz: List<KClass<*>> = instance::class.superclasses + instance::class

    val isTypeOf: (kClass: KClass<*>) -> (Boolean) = { kClass ->
        clazz.contains(kClass)
    }
}
