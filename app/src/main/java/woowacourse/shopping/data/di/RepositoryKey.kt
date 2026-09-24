package woowacourse.shopping.data.di

import kotlin.reflect.KClass

data class RepositoryKey(
    val type: KClass<*>,
    val qualifier: KClass<out Annotation>?,
)
