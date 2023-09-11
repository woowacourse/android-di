package woowacourse.shopping.data.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object Injector {
    lateinit var container: Container

    inline fun <reified T : Any> inject(): T {
        val instance = injectConstructor<T>()
        val properties: List<KProperty<*>> =
            T::class.declaredMemberProperties.filter { it.hasAnnotation<Inject>() }
        val propertyDependencies =
            provideInstances(properties.map { it.returnType.jvmErasure })
        val propertyDependenciesMap = properties.associateWith {
            propertyDependencies[it.returnType.jvmErasure] ?: throw NoSuchElementException()
        }
        injectField(instance, properties, propertyDependenciesMap)
        return instance
    }

    fun provideInstances(requireTypes: List<KClass<*>>): Map<KClass<*>, Any> {
        val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()
        requireTypes.forEach {
            dependencies[it] =
                container.getInstance(it) ?: throw NoSuchElementException()
        }
        return dependencies
    }

    inline fun <reified T : Any> injectConstructor(): T {
        val constructor = requireNotNull(T::class.primaryConstructor)
        val parameters: List<KParameter> =
            constructor.parameters.filter { it.hasAnnotation<Inject>() }
        val parameterDependencies: Map<KClass<*>, Any> =
            provideInstances(parameters.map { it.type.jvmErasure })
        val parameterDependenciesMap = parameters.associateWith {
            parameterDependencies[it.type.jvmErasure] ?: throw NoSuchElementException()
        }
        return constructor.callBy(parameterDependenciesMap)
    }

    inline fun <reified T> injectField(
        instance: T,
        properties: List<KProperty<*>>,
        propertyDependencies: Map<KProperty<*>, Any>
    ) {
        properties.forEach {
            val property = it as KMutableProperty<*>
            property.isAccessible = true
            property.setter.call(instance, propertyDependencies[it])
        }
    }
}