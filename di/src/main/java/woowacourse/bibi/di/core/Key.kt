package woowacourse.bibi.di.core

import kotlin.reflect.KClass

internal data class Key(
    val type: KClass<*>,
    val qualifier: KClass<out Annotation>?,
)
