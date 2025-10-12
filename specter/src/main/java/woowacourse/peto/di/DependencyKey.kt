package woowacourse.peto.di

import kotlin.reflect.KType

internal data class DependencyKey(
    val type: KType,
    val qualifier: String? = null,
)
