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
    private val providers = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> provideModule(moduleKClazz: KClass<T>) {
        val module = moduleKClazz.objectInstance ?: return

        moduleKClazz.memberProperties.forEach { property ->
            property.isAccessible = true
            val instance = property.get(module) ?: return@forEach
            providers[property.returnType.classifier as KClass<*>] = instance
        }
    }

    fun <T : Any> resolve(
        clazz: KClass<T>,
        overrides: Map<KClass<*>, Any> = emptyMap(),
    ): T = (providers[clazz] as? T) ?: createInstance(clazz, overrides)

    private fun <T : Any> createInstance(
        clazz: KClass<T>,
        overrides: Map<KClass<*>, Any>,
    ): T {
        val constructor =
            requireNotNull(clazz.primaryConstructor) { "${clazz.simpleName}에 public 생성자가 없습니다." }
        val instance = constructor.resolveConstructorParameters(overrides).let(constructor::callBy)
        clazz.createPropertyInstance(instance, overrides)
        return instance
    }

    private fun <T : Any> KFunction<T>.resolveConstructorParameters(overrides: Map<KClass<*>, Any>): Map<KParameter, Any?> =
        parameters
            .filter { parameter -> parameter.hasAnnotation<MyInjector>() }
            .associateWith { param ->
                val kClazz = param.type.classifier as KClass<*>
                overrides[kClazz] ?: resolve(kClazz, overrides)
            }

    private fun <T : Any> KClass<T>.createPropertyInstance(
        instance: T,
        overrides: Map<KClass<*>, Any>,
    ) = declaredMemberProperties
        .filter { property -> property.hasAnnotation<MyInjector>() }
        .forEach { property ->
            property.isAccessible = true
            val k = property.returnType.classifier as KClass<*>
            val propertyInstance = overrides[k] ?: resolve(k, overrides)
            val mutableProperty = requireNotNull(property as KMutableProperty1<T, Any>)
            mutableProperty.set(instance, propertyInstance)
        }
}
