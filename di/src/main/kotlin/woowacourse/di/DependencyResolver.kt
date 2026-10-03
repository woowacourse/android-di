package woowacourse.di

import kotlin.reflect.KClass

interface DependencyResolver {
    fun <T : Any> create(type: KClass<T>): T
}
