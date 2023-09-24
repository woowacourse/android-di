package com.woowacourse.shopping

import android.content.Context
import com.woowacourse.shopping.annotation.Retain
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

object InstancesContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun isEmpty(): Boolean = instances.isEmpty()

    fun saveInstances(context: Context) {
        context::class.declaredMemberProperties.forEach { property ->
            if (property.hasAnnotation<Retain>().not()) return
            instances[property::class] = property
        }
    }

    fun setInstances(context: Context) {
        val mutableProperties =
            context::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
        mutableProperties.forEach { property ->
            val instance = instances[property::class]
            property.isAccessible = true
            property.setter.call(context, instance)
        }
    }

    fun clear() {
        instances.clear()
    }
}
