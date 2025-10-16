package woowacourse.di

import kotlin.reflect.KClass

typealias Provider<T> = () -> T

enum class ScopeType {
    NONE,
    APPLICATION,
    ACTIVITY,
    FRAGMENT,
    SERVICE,
    VIEW_MODEL,
}

class ScopeContext private constructor(
    private val identifiers: Map<ScopeType, Any>,
) {
    fun identifierOf(scopeType: ScopeType): Any =
        identifiers[scopeType]
            ?: error("Scope ${scopeType.name} is not registered in this context")

    fun plus(
        scopeType: ScopeType,
        identifier: Any,
    ): ScopeContext = ScopeContext(identifiers + (scopeType to identifier))

    companion object {
        internal const val APPLICATION_IDENTIFIER: String = "woowacourse.di.application"

        fun application(): ScopeContext = ScopeContext(mapOf(ScopeType.APPLICATION to APPLICATION_IDENTIFIER))

        fun activity(activity: Any): ScopeContext = application().plus(ScopeType.ACTIVITY, activity)

        fun fragment(fragment: Any): ScopeContext = application().plus(ScopeType.FRAGMENT, fragment)

        fun fragment(
            activity: Any,
            fragment: Any,
        ): ScopeContext = activity(activity).plus(ScopeType.FRAGMENT, fragment)

        fun service(service: Any): ScopeContext = application().plus(ScopeType.SERVICE, service)

        fun viewModel(identifier: Any): ScopeContext = application().plus(ScopeType.VIEW_MODEL, identifier)
    }
}

open class Container {
    private data class Key(
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>?,
    )

    private data class ScopedProvider(
        val scopeType: ScopeType,
        val creator: () -> Any,
    )

    private data class ScopedInstanceKey(
        val key: Key,
        val scopeType: ScopeType,
        val identifier: Any,
    )

    private val providers: MutableMap<Key, ScopedProvider> = mutableMapOf()
    private val scopedInstances: MutableMap<ScopedInstanceKey, Any> = mutableMapOf()
    private val currentScopeContext = ThreadLocal<ScopeContext>()

    fun <T : Any> bindSingleton(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        creator: Provider<T>,
    ) {
        bindScoped(type = type, qualifier = qualifier, scopeType = ScopeType.APPLICATION, creator = creator)
    }

    fun <T : Any> bindScoped(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        scopeType: ScopeType,
        creator: Provider<T>,
    ) {
        val key = Key(type, qualifier)
        providers[key] = ScopedProvider(scopeType) { creator() }
    }

    fun clearScope(
        scopeType: ScopeType,
        identifier: Any,
    ) {
        val keysToRemove =
            scopedInstances
                .filterKeys { it.scopeType == scopeType && it.identifier == identifier }
                .keys
        keysToRemove.forEach { scopedInstances.remove(it) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        scopeContext: ScopeContext? = null,
    ): T {
        val key = Key(type, qualifier)
        val provider =
            providers[key]
                ?: error("No provider for ${type.qualifiedName} with qualifier=${qualifier?.qualifiedName}")
        val effectiveContext =
            scopeContext
                ?: currentScopeContext.get()
                ?: ScopeContext.application()

        return when (provider.scopeType) {
            ScopeType.NONE -> invokeWithScopeContext(effectiveContext) { provider.creator.invoke() } as T
            else -> {
                val identifier = effectiveContext.identifierOf(provider.scopeType)
                val instanceKey = ScopedInstanceKey(key, provider.scopeType, identifier)
                scopedInstances.getOrPut(instanceKey) {
                    invokeWithScopeContext(effectiveContext) { provider.creator.invoke() }
                } as T
            }
        }
    }

    inline fun <reified T : Any> get(
        qualifier: KClass<out Annotation>? = null,
        scopeContext: ScopeContext? = null,
    ): T = get(T::class, qualifier, scopeContext)

    protected fun requireScopeContext(): ScopeContext = currentScopeContext.get() ?: error("No scope context is currently active")

    private fun <T> invokeWithScopeContext(
        scopeContext: ScopeContext,
        block: () -> T,
    ): T {
        val previous = currentScopeContext.get()
        currentScopeContext.set(scopeContext)
        return try {
            block()
        } finally {
            if (previous == null) {
                currentScopeContext.remove()
            } else {
                currentScopeContext.set(previous)
            }
        }
    }
}
