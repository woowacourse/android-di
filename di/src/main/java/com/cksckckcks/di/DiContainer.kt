package com.cksckckcks.di

import kotlin.reflect.KClass

class DiContainer(
    private val defaultScopeType: ScopeType = ScopeType("application"),
) {
    private val bindings = mutableMapOf<BindingKey, Binding>()
    private val scopes = mutableMapOf<ScopeKey, ScopedContainer>()
    val applicationScope = ScopedContainer(this, defaultScopeType, "default")

    fun <T : Any> register(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        scope: ScopeType = defaultScopeType,
        factory: () -> T,
    ) {
        bindings[BindingKey(type, qualifier)] = Binding(scope, factory)
    }

    fun getInstance(
        targetClass: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any? = applicationScope.getInstance(targetClass, qualifier)

    fun saveInstance(
        key: KClass<*>,
        value: Any,
        qualifier: KClass<out Annotation>? = null,
    ) {
        applicationScope.saveInstance(key, value, qualifier)
    }

    fun openScope(
        type: ScopeType,
        id: String,
    ): ScopedContainer {
        require(type != defaultScopeType) { "기본 스코프는 컨테이너가 관리합니다." }
        val key = ScopeKey(type, id)
        return scopes.getOrPut(key) { ScopedContainer(this, type, id) }
    }

    fun closeScope(
        type: ScopeType,
        id: String,
    ) {
        scopes.remove(ScopeKey(type, id))?.release()
    }

    internal fun getInstance(
        targetClass: KClass<*>,
        qualifier: KClass<out Annotation>?,
        scope: ScopedContainer,
    ): Any? {
        val key =
            if (qualifier != null) {
                BindingKey(targetClass, qualifier)
            } else {
                val candidates =
                    (bindings.keys + scope.keysFor(targetClass) + applicationScope.keysFor(targetClass))
                        .filter { it.type == targetClass }
                        .distinct()
                if (candidates.size > 1) {
                    val names = candidates.map { it.qualifier?.simpleName ?: "unqualified" }.sorted()
                    throw IllegalArgumentException(
                        "[Qualifier 지정 필요] ${targetClass.simpleName}에 여러 구현체가 등록되어 있습니다: $names.",
                    )
                }
                candidates.singleOrNull() ?: return null
            }

        val binding = bindings[key]
        val owner = binding?.let { findScope(it.scope, scope) } ?: scope
        owner.getStoredInstance(key)?.let { return it }
        if (binding == null) return applicationScope.getStoredInstance(key)
        val instance = binding.factory()
        owner.store(key, instance)
        return instance
    }

    internal fun saveInstance(
        key: BindingKey,
        value: Any,
        scope: ScopedContainer,
    ) {
        val owner = bindings[key]?.let { findScope(it.scope, scope) } ?: scope
        owner.store(key, value)
    }

    private fun findScope(
        type: ScopeType,
        scope: ScopedContainer,
    ): ScopedContainer {
        if (type == defaultScopeType) return applicationScope
        require(type == scope.type) { "${type.name} 스코프가 필요합니다." }
        return scope
    }

    private data class Binding(
        val scope: ScopeType,
        val factory: () -> Any,
    )

    private data class ScopeKey(
        val type: ScopeType,
        val id: String,
    )
}
