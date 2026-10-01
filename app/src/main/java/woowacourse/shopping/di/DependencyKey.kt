package woowacourse.shopping.di

import kotlin.reflect.KClass

data class DependencyKey(
    val kClass: KClass<*>,
    val qualifier: KClass<out Annotation>? = null,
)
