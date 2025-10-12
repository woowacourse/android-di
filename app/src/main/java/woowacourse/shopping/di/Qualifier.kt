package woowacourse.shopping.di

import kotlin.reflect.KClass

data class Qualifier(
    val type: KClass<*>,
    val name: String? = null,
)
