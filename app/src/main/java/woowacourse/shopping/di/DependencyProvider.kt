package woowacourse.shopping.di

import kotlin.reflect.KClass

interface DependencyProvider {
    fun <T : Any> getInstance(key: KClass<*>): T
}
