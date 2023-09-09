package com.buna.di.injector

import com.buna.di.annotation.Inject
import com.buna.di.util.validateHasPrimaryConstructor
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object DependencyInjector {
    private val typeConverter = mutableMapOf<DependencyKey, KType>()
    private val cache = Cache()

    fun module(module: Module) {
        val providers = module::class.declaredMemberFunctions
        providers.forEach { provider -> cache.caching(module, provider) }
    }

    fun type(superClass: KClass<*>, subClass: KClass<*>) {
        val superType = superClass.starProjectedType
        val annotation = subClass.annotations.firstOrNull()

        val dependencyKey = DependencyKey(superType, annotation)
        val subType = subClass.starProjectedType

        typeConverter[dependencyKey] = subType
        cache.caching(dependencyKey)
    }

    fun <T : Any> inject(clazz: KClass<T>): T {
        val dependencyKey = DependencyKey.createDependencyKey(clazz)
        val cached = cache.get(dependencyKey)
        if (cached != null) return cached as T

        val primaryConstructor = clazz.validateHasPrimaryConstructor()
        val parameters = primaryConstructor.parameters

        val arguments = parameters.map { parameter ->
            val paramDependencyKey = DependencyKey.createDependencyKey(parameter)
            val paramType = parameter.type
            val subType = typeConverter[paramDependencyKey] ?: paramType
            inject(subType.jvmErasure)
        }

        val dependency = primaryConstructor.call(*arguments.toTypedArray())

        clazz.memberProperties.forEach { property: KProperty<*> ->
            if (!property.hasAnnotation<Inject>()) return@forEach
            if (property !is KMutableProperty<*>) return@forEach
            property.isAccessible = true

            val propertyType = property.returnType
            val propertyDependencyKey = DependencyKey.createDependencyKey(property)
            val subType = typeConverter[propertyDependencyKey] ?: propertyType
            val propertyInstance = inject(subType.jvmErasure)
            cache.caching(propertyDependencyKey, propertyInstance)

            property.setter.call(dependency, propertyInstance)
        }

        return dependency
    }
}
