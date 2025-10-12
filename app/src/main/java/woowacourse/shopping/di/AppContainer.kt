package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object AppContainer {
    private val providers = mutableMapOf<KClass<out Any>, Any>()

    fun <T : Any> provide(
        clazz: KClass<T>,
        instance: T,
    ) {
        providers[clazz] = instance
    }

    fun <T : Any> provideModule(moduleKClazz: KClass<T>) {
        val module = moduleKClazz.objectInstance ?: return

        moduleKClazz.memberProperties.forEach { property ->
            property.isAccessible = true
            val instance = property.get(module) ?: return@forEach
            providers[property.returnType.classifier as KClass<out Any>] = instance
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(
        clazz: KClass<T>,
        overrides: Map<KClass<out Any>, Any> = emptyMap(),
    ): T {
        val over = overrides[clazz] as? T
        if (over != null) return over

        val provided = providers[clazz] as? T
        if (provided != null) return provided

        return createInstance(clazz, overrides)
    }

    inline fun <reified T : Any> resolve(overrides: Map<KClass<out Any>, Any> = emptyMap()): T = resolve(T::class, overrides)

    private fun <T : Any> createInstance(
        clazz: KClass<T>,
        overrides: Map<KClass<out Any>, Any>,
    ): T {
        val constructor =
            requireNotNull(clazz.primaryConstructor) { "${clazz.simpleName}에 public 생성자가 없습니다." }
        val instance = constructor.resolveConstructorParameters(overrides).let(constructor::callBy)
        clazz.createPropertyInstance(instance, overrides)
        return instance
    }

    private fun <T : Any> KFunction<T>.resolveConstructorParameters(overrides: Map<KClass<out Any>, Any>): Map<KParameter, Any> {
        isAccessible = true
        if (hasAnnotation<MyInjector>().not()) return emptyMap()

        return parameters.associateWith { param ->
            val kClazz = param.type.classifier as KClass<out Any>
            overrides[kClazz] ?: resolve(kClazz, overrides)
        }
    }

    private fun <T : Any> KClass<T>.createPropertyInstance(
        instance: T,
        overrides: Map<KClass<out Any>, Any>,
    ) = declaredMemberProperties
        .filter { property -> property.hasAnnotation<MyInjector>() }
        .forEach { property ->
            property.isAccessible = true
            val k = property.returnType.classifier as KClass<out Any>
            val propertyInstance = overrides[k] ?: resolve(k, overrides)
            val mutableProperty = requireNotNull(property as KMutableProperty1<T, Any>)
            mutableProperty.set(instance, propertyInstance)
        }
}
