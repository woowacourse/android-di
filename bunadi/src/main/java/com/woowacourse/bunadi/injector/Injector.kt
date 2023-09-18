package com.woowacourse.bunadi.injector

import com.woowacourse.bunadi.annotation.Inject
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import com.woowacourse.bunadi.util.createInstance
import com.woowacourse.bunadi.util.parseFromQualifier
import com.woowacourse.bunadi.util.validateHasPrimaryConstructor
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

abstract class Injector(
    val cache: Cache = DefaultCache(),
) {
    abstract val scopeAnnotation: KClass<out Annotation>

    fun <T : Any> inject(clazz: KClass<T>): T {
        val dependencyKey = DependencyKey.createDependencyKey(clazz)
        val cached = cache[dependencyKey]
        if (cached != null) return cached as T

        val primaryConstructor = clazz.validateHasPrimaryConstructor()
        val dependency = primaryConstructor.createInstance(this)
        injectMemberProperties(clazz, dependency)

        if (clazz.hasScopeAnnotation()) {
            caching(dependencyKey, dependency)
        }
        return dependency
    }

    fun <T : Any> injectMemberProperties(clazz: KClass<T>, instance: Any) {
        clazz.memberProperties.forEach { property ->
            if (!property.hasAnnotation<Inject>()) return@forEach
            if (property !is KMutableProperty<*>) return@forEach
            property.isAccessible = true

            val dependencyKey = DependencyKey.createDependencyKey(property)
            val realType = property.parseFromQualifier()
            val propertyInstance = inject(realType ?: property.returnType.jvmErasure)

            if (property.hasScopeAnnotation()) {
                caching(dependencyKey, propertyInstance)
            }
            property.setter.call(instance, propertyInstance)
        }
    }

    private fun <T : Any> KClass<T>.hasScopeAnnotation(): Boolean {
        return this.annotations.any { it.annotationClass == scopeAnnotation }
    }

    private fun <T> KProperty1<T, *>.hasScopeAnnotation(): Boolean {
        return this.annotations.any { it.annotationClass == scopeAnnotation }
    }

    fun caching(dependencyKey: DependencyKey, dependency: Any?) {
        cache.caching(dependencyKey, dependency)
    }

    fun clear() {
        cache.clear()
    }
}
