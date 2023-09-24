package com.bandal.fullmoon

import com.bandal.fullmoon.DIError.NotFoundCreateFunction
import com.bandal.fullmoon.DIError.NotFoundPrimaryConstructor
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class FullMoonInjector(private val appContainer: AppContainer) {

    private val containers: HashMap<String, AppContainer?> = HashMap()

    fun <T : Any> inject(kClass: KClass<T>): T {
        val constructor = kClass.primaryConstructor ?: throw NotFoundPrimaryConstructor()
        val parameters: List<Any> = constructor.parameters.map { kParameter ->
            appContainer.getSavedInstance(kParameter.getKey())
                ?: throw NotFoundCreateFunction(kParameter.getKey())
        }
        val instance = constructor.call(*parameters.toTypedArray())
        return instance.apply { injectFields(kClass, this) }
    }

    fun <T : Any> injectFields(kClass: KClass<out T>, instance: T) {
        instance::class.declaredMemberProperties
            .filter { it.hasAnnotation<FullMoonInject>() || it.hasAnnotation<Qualifier>() }
            .forEach { property ->
                val container = containers[kClass.simpleName.toString()]
                val injectValue = appContainer.getSavedInstance(property.getKey())
                    ?: container?.getSavedInstance(property.getKey())
                property.isAccessible = true
                (property as KMutableProperty<*>).setter.call(instance, injectValue)
            }
    }

    fun hasContainer(tag: String): Boolean = containers[tag] != null

    fun setContainer(tag: String, container: AppContainer) {
        containers[tag] = container
    }

    fun removeContainer(tag: String) {
        containers[tag] = null
    }
}
