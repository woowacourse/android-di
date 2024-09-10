package woowacourse.shopping.di

import kotlin.reflect.KClass

interface ClassLoader {
    fun getSubclasses(parent: KClass<*>): List<KClass<*>>
}
