package woowacourse.di

import kotlin.reflect.KClass

internal data class DependencyKey(
    val type: KClass<*>,
    val qualifier: KClass<out Annotation>? = null,
) {
    override fun toString(): String = type.simpleName.orEmpty() + qualifier?.let { "[@${it.simpleName}]" }.orEmpty()
}
