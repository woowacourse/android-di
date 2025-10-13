package com.shopping.di

import com.shopping.di.definition.DefinitionInformation
import com.shopping.di.definition.DefinitionKey
import com.shopping.di.definition.Kind
import com.shopping.di.definition.Qualifier
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

private const val ERROR_NOT_FOUND_KEY = "자동 주입할 데이터를 찾을 수 없습니다. kclass = %s"
private const val ERROR_EXIST_KEY = "이미 등록된 Definition 입니다, key = %s"

object InjectContainer {
    private val definitions: MutableMap<DefinitionKey, DefinitionInformation<*>> = mutableMapOf()
    private var singletons: ConcurrentHashMap<DefinitionKey, Any> = ConcurrentHashMap()

    fun init(vararg modules: InjectionModule) {
        definitions.clear()
        singletons.clear()

        modules.forEach { module: InjectionModule -> module.provideDefinitions(this) }
    }

    inline fun <reified T : Any> registerSingleton(
        qualifier: Qualifier? = null,
        noinline provider: (InjectContainer) -> Provider<T>,
    ) {
        register(T::class, qualifier, Kind.SINGLETON, provider)
    }

    inline fun <reified T : Any> registerFactory(
        qualifier: Qualifier? = null,
        noinline provider: (InjectContainer) -> Provider<T>,
    ) {
        register(T::class, qualifier, Kind.FACTORY, provider)
    }

    fun <T : Any> register(
        kclass: KClass<T>,
        qualifier: Qualifier?,
        kind: Kind,
        provider: (InjectContainer) -> Provider<T>,
    ) {
        val key = DefinitionKey(kclass, qualifier)

        check(!definitions.containsKey(key)) { ERROR_EXIST_KEY.format(key.toString()) }

        definitions[key] = DefinitionInformation(kclass, qualifier, kind, provider)
    }

    inline fun <reified T : Any> get(qualifier: Qualifier? = null): T = get(T::class, qualifier)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        kclass: KClass<T>,
        qualifier: Qualifier? = null,
    ): T {
        val key = DefinitionKey(kclass, qualifier)
        val information: DefinitionInformation<*> =
            checkNotNull(definitions[key]) { ERROR_NOT_FOUND_KEY.format(key.kclass) }
        return when (information.kind) {
            Kind.SINGLETON -> {
                singletons.computeIfAbsent(key) {
                    val instance =
                        (information.provider as (InjectContainer) -> Provider<T>)(this).get()
                    DependencyInjector.injectFields(instance)
                } as T
            }

            Kind.FACTORY -> {
                val instance =
                    (information.provider as (InjectContainer) -> Provider<T>)(this).get()
                DependencyInjector.injectFields(instance)
            }
        }
    }

    fun hasDefinition(
        kClass: KClass<*>,
        qualifier: Qualifier? = null,
    ): Boolean {
        val key = DefinitionKey(kClass, qualifier)
        return definitions[key] != null
    }
}
