package woowacourse.di

import kotlin.reflect.KClass

internal sealed interface DependencyDefinition {
    data class Binding(
        val implementationType: KClass<*>,
        val scopeType: ScopeType,
    ) : DependencyDefinition

    data class Instance(
        val value: Any,
    ) : DependencyDefinition
}

internal data class DependencyKey(
    val type: KClass<*>,
    val qualifier: KClass<out Annotation>?,
) {
    val displayName: String
        get() = listOfNotNull(qualifier?.simpleName, type.simpleName).joinToString(" ")
}
