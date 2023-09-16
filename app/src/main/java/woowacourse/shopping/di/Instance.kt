package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

data class Instance<T : Any>(val value: T) {

    val clazz: List<KClass<*>> = value::class.superclasses + value::class
}
