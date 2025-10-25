package woowacourse.peto.di

import kotlin.reflect.KType

data class DependencyKey(
    val type: KType,
    val qualifier: String? = null,
)
