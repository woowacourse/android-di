package woowacourse.di

import kotlin.reflect.KClass

interface DIResolver {
    fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T

    fun <T : Any> create(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T

    fun openScope(
        type: ScopeType,
        id: String,
    ): ScopedContainer
}
