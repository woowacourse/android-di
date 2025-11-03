package woowacourse.shopping.di

import kotlin.reflect.KClass

data class DependencyKey(
    val type: KClass<*>,
    val qualifier: String? = null,
)
