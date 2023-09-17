package com.mission.androiddi.component.activity.retain

import com.mission.androiddi.scope.ActivityScope
import com.woowacourse.bunadi.annotation.Inject
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import com.woowacourse.bunadi.injector.DependencyKey
import com.woowacourse.bunadi.injector.Injector
import com.woowacourse.bunadi.util.createInstance
import com.woowacourse.bunadi.util.parseFromQualifier
import com.woowacourse.bunadi.util.validateHasPrimaryConstructor
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class RetainedActivityDependencyInjector(
    cache: Cache = DefaultCache(),
) : Injector(cache) {

    override fun <T : Any> inject(clazz: KClass<T>): T {
        val dependencyKey = DependencyKey.createDependencyKey(clazz)
        val cached = cache[dependencyKey]
        if (cached != null) return cached as T

        val primaryConstructor = clazz.validateHasPrimaryConstructor()
        val dependency = primaryConstructor.createInstance(this)
        injectMemberProperties(clazz, dependency)

        if (clazz.hasAnnotation<ActivityScope>()) {
            caching(dependencyKey, dependency)
        }
        return dependency
    }

    override fun <T : Any> injectMemberProperties(clazz: KClass<T>, instance: Any) {
        clazz.memberProperties.forEach { property ->
            if (!property.hasAnnotation<Inject>()) return@forEach
            if (property !is KMutableProperty<*>) return@forEach
            property.isAccessible = true

            val dependencyKey = DependencyKey.createDependencyKey(property)
            val realType = property.parseFromQualifier()
            val propertyInstance = inject(realType ?: property.returnType.jvmErasure)

            if (property.hasAnnotation<ActivityScope>()) {
                caching(dependencyKey, propertyInstance)
            }
            property.setter.call(instance, propertyInstance)
        }
    }
}
