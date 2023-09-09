package com.buna.di.injector

import com.buna.di.annotation.Inject
import com.buna.di.module.Module
import com.buna.di.util.createInstance
import com.buna.di.util.diAssistant.Cache
import com.buna.di.util.diAssistant.SubTypeConverter
import com.buna.di.util.validateHasPrimaryConstructor
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object DependencyInjector {
    private val subTypeConverter = SubTypeConverter()
    private val cache = Cache()

    fun <T : Any> inject(clazz: KClass<T>): T {
        val cached = cache.get(DependencyKey.createDependencyKey(clazz))
        if (cached != null) return cached as T

        val primaryConstructor = clazz.validateHasPrimaryConstructor()
        val dependency = primaryConstructor.createInstance(subTypeConverter)

        injectMemberProperties(clazz, dependency)
        return dependency
    }

    private fun <T : Any> injectMemberProperties(clazz: KClass<T>, instance: T) {
        clazz.memberProperties.forEach { property ->
            if (!property.hasAnnotation<Inject>()) return@forEach
            if (property !is KMutableProperty<*>) return@forEach
            property.isAccessible = true

            val dependencyKey = DependencyKey.createDependencyKey(property)
            val propertyType = property.returnType
            val subType = subTypeConverter.convertType(dependencyKey, propertyType)
            val propertyInstance = inject(subType.jvmErasure)

            cache.caching(dependencyKey, propertyInstance)
            property.setter.call(instance, propertyInstance)
        }
    }

    fun module(module: Module) {
        val providers = module::class.declaredMemberFunctions
        providers.forEach { provider -> cache.caching(module, provider) }
    }

    fun type(superClass: KClass<*>, subClass: KClass<*>) {
        val superType = superClass.starProjectedType
        val annotation = subClass.annotations.firstOrNull()

        val dependencyKey = DependencyKey(superType, annotation)
        val subType = subClass.starProjectedType

        subTypeConverter.saveType(dependencyKey, subType)
        cache.caching(dependencyKey)
    }

    fun clear() {
        subTypeConverter.clear()
        cache.clear()
    }
}
