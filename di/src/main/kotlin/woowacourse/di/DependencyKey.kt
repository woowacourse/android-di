package woowacourse.di

import kotlin.reflect.KClass

internal data class DependencyKey(
    val type: KClass<*>,
    val qualifier: KClass<out Annotation>?,
)
