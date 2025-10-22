package woowacourse.bibi.di.core

import kotlin.reflect.KClass

class ContainerBuilder {
    private val providers = mutableMapOf<Key, Binding>()

    fun <A : Any> register(
        abstractType: KClass<A>,
        provider: () -> A,
    ): ContainerBuilder = register(abstractType, null, AppScope::class, provider)

    fun <A : Any> register(
        abstractType: KClass<A>,
        scope: KClass<out Annotation>,
        provider: () -> A,
    ): ContainerBuilder = register(abstractType, null, scope, provider)

    fun <A : Any> register(
        abstractType: KClass<A>,
        qualifier: KClass<out Annotation>?,
        scope: KClass<out Annotation>,
        provider: () -> A,
    ): ContainerBuilder = apply { providers[Key(abstractType, qualifier)] = Binding(provider as () -> Any, scope) }

    fun build(): Container {
        val providersSnap = providers.toMap()
        return AppContainer(
            parent = null,
            scope = AppScope::class,
            providers = providersSnap,
        )
    }
}
