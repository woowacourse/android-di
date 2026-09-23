package woowacourse.di

import kotlin.reflect.KClass

data class DependencyKey(
    val type: KClass<*>,
    val qualifier: KClass<out Annotation>? = null,
)
