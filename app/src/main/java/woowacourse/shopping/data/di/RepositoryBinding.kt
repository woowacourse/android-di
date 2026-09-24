package woowacourse.shopping.data.di

import kotlin.reflect.KClass

data class RepositoryBinding(
    val type: KClass<*>,
    val implementation: KClass<*>,
    val qualifier: KClass<out Annotation>? = null,
)

