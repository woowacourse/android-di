package woowacourse.shopping.di

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import woowacourse.shopping.di.definition.DefinitionInformation
import woowacourse.shopping.di.definition.DefinitionKey
import woowacourse.shopping.di.definition.Kind
import woowacourse.shopping.di.definition.Qualifier
import woowacourse.shopping.di.module.InjectionModule

private const val ERROR_NOT_FOUND_KEY = "자동 주입할 데이터를 찾을 수 없습니다. kclass = %s"

object AppInjector {
    private val definitions: MutableMap<DefinitionKey, DefinitionInformation<*>> = mutableMapOf()
    private var singletons: ConcurrentHashMap<DefinitionKey, Any> = ConcurrentHashMap()

    fun init(modules: List<InjectionModule>) {
        modules.forEach { module: InjectionModule ->
            val definitions = module.provideDefinitions()
            registerDefinitions(definitions)
        }
    }

    private fun registerDefinitions(definitions: List<DefinitionInformation<*>>) {
        definitions.forEach { definitionInformation: DefinitionInformation<*> ->
            register(definitionInformation)
        }
    }

    private fun register(definitionInformation: DefinitionInformation<*>) {
        val key = DefinitionKey(definitionInformation.kclass, definitionInformation.qualifier)
        definitions[key] = definitionInformation
    }

    inline fun <reified T : Any> registerSingleton(
        qualifier: Qualifier? = null,
        noinline provider: (AppInjector) -> Provider<T>,
    ) {
        register(T::class, qualifier, Kind.SINGLETON, provider)
    }

    inline fun <reified T : Any> registerFactory(
        qualifier: Qualifier? = null,
        noinline provider: (AppInjector) -> Provider<T>,
    ) {
        register(T::class, qualifier, Kind.FACTORY, provider)
    }

    fun <T : Any> register(
        kclass: KClass<T>,
        qualifier: Qualifier?,
        kind: Kind,
        provider: (AppInjector) -> Provider<T>,
    ) {
        val key = DefinitionKey(kclass, qualifier)
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
                    (information.provider as (AppInjector) -> Provider<T>)(this).get()
                } as T
            }

            Kind.FACTORY -> {
                (information.provider as (AppInjector) -> Provider<T>)(this).get()
            }
        }
    }

    fun hasDefinition(
        kClass: KClass<*>,
        qualifier: Qualifier?,
    ): Boolean {
        val key = DefinitionKey(kClass, qualifier)
        return definitions[key] != null
    }
}
