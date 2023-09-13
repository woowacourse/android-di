package com.woowacourse.bunadi.injector

import com.woowacourse.bunadi.annotation.Inject
import com.woowacourse.bunadi.annotation.Qualifier
import com.woowacourse.bunadi.annotation.Singleton
import com.woowacourse.bunadi.module.Module
import com.woowacourse.bunadi.util.core.Cache
import com.woowacourse.bunadi.util.core.SubTypeConverter
import com.woowacourse.bunadi.util.createInstance
import com.woowacourse.bunadi.util.validateHasPrimaryConstructor
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
        val dependencyKey = DependencyKey.createDependencyKey(clazz)
        val cached = cache[dependencyKey]
        if (cached != null) return cached as T

        val primaryConstructor = clazz.validateHasPrimaryConstructor()
        val dependency = primaryConstructor.createInstance(subTypeConverter)

        injectMemberProperties(clazz, dependency)

        if (clazz.hasAnnotation<Singleton>()) {
            caching(dependencyKey, dependency)
        }
        return dependency
    }

    private fun <T : Any> injectMemberProperties(clazz: KClass<T>, instance: T) {
        clazz.memberProperties.forEach { property ->
            if (!property.hasAnnotation<Inject>()) return@forEach
            if (property !is KMutableProperty<*>) return@forEach
            property.isAccessible = true

            val dependencyKey = DependencyKey.createDependencyKey(property)
            val propertyType = property.returnType

            val annotation = property.annotations.find { it !is Inject }
            val qualifier = annotation?.annotationClass
            val qualifier2 = qualifier?.annotations?.find { it is Qualifier } as? Qualifier
            val realType = qualifier2?.clazz
            val propertyInstance = inject(realType ?: propertyType.jvmErasure)

            if (clazz.hasAnnotation<Singleton>()) {
                caching(dependencyKey, propertyInstance)
            }
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

    fun caching(dependencyKey: DependencyKey, dependency: Any? = null) {
        cache.caching(dependencyKey, dependency)
    }

    fun clear() {
        subTypeConverter.clear()
        cache.clear()
    }
}
