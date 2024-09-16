package com.kmlibs.supplin

import com.kmlibs.supplin.annotations.Supply
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object InstanceSupplier {
    /**
     * For injecting fields with @Supply annotation.
     */
    fun <T : Any> injectFields(
        clazz: KClass<T>,
        targetInstance: Any,
    ) {
        clazz.memberProperties.filter { field ->
            field.hasAnnotation<Supply>()
        }.forEach { targetField ->
            injectSingleField(targetField as KMutableProperty<*>, targetInstance)
        }
    }

    private fun injectSingleField(
        property: KMutableProperty<*>,
        targetInstance: Any,
    ) {
        property.isAccessible = true
        property.setter.call(targetInstance, findInstanceOf(property))
    }

    private fun findInstanceOf(property: KProperty<*>): Any {
        return Injector.instanceContainer.instanceOf(property)
    }
}
