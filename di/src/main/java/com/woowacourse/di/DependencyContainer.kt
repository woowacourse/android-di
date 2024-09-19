package com.woowacourse.di

import com.woowacourse.di.annotation.Inject
import com.woowacourse.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

typealias DependencyKey = Pair<KClass<*>, String?>

object DependencyContainer {
    private val instances = mutableMapOf<DependencyKey, Any>()

    fun <T : Any> addInstance(
        classType: KClass<*>,
        instance: T,
        qualifier: String? = null,
    ) {
        val key: DependencyKey = classType to qualifier
        if (instances.containsKey(key)) return
        instances[key] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> instance(
        classType: KClass<*>,
        qualifier: String? = null,
    ): T {
        val key: DependencyKey = classType to qualifier
        return instances[key] as? T ?: createInstance(classType)
    }

    fun <T : Any> createInstance(modelClass: KClass<*>): T {
        val constructor =
            modelClass.constructors.firstOrNull()
                ?: throw IllegalArgumentException("Unknown modelClass")

        val params =
            constructor.parameters.map { parameter ->
                val paramClass =
                    parameter.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("Unknown parameter type: ${parameter.type}")
                instance<Any>(paramClass)
            }.toTypedArray()

        val instance = constructor.call(*params) as T
        injectProperty(instance)
        return instance
    }

    private fun <T : Any> injectProperty(instance: T) {
        instance::class.declaredMemberProperties
            .filter { isInjectableProperty(it) }
            .forEach { property ->
                val qualifier = property.findAnnotation<Qualifier>()?.value
                val dependencyClass = instance(property.returnType.jvmErasure, qualifier) as Any
                property.isAccessible = true
                (property as KMutableProperty<*>).setter.call(instance, dependencyClass)
            }
    }

    private fun isInjectableProperty(property: KProperty<*>): Boolean {
        return property.hasAnnotation<Inject>() && property is KMutableProperty<*>
    }

    fun clear() {
        instances.clear()
    }
}
