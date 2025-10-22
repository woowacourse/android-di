package woowacourse.bibi.di.core

import kotlin.reflect.KClass

internal data class Binding(
    val provider: () -> Any,
    val scope: KClass<out Annotation>,
)
