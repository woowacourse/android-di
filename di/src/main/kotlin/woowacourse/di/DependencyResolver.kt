package woowacourse.di

import kotlin.reflect.KClass

interface DependencyResolver {
    fun <T : Any> create(type: KClass<T>): T
}

interface ScopedDependencyResolver : DependencyResolver {
    fun openScope(
        key: ScopeKey,
        id: String? = null,
    ): DependencyScope

    fun <T : Any> createInScope(
        type: KClass<T>,
        scope: DependencyScope,
    ): T

    fun <T : Any> getInScope(
        type: KClass<T>,
        scope: DependencyScope,
        qualifier: KClass<out Annotation>? = null,
    ): T
}
