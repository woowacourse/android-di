package com.zzang.di

import com.zzang.di.annotation.Inject
import com.zzang.di.annotation.QualifierType
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

object DependencyInjector {
    fun <T : Any> inject(targetClass: KClass<T>): T {
        val instance = DIContainer.resolve(targetClass)
        injectDependencies(instance)
        return instance
    }

    fun <T : Any> injectDependencies(target: T) {
        val kClass = target::class

        kClass.declaredMemberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .filter { property -> property.javaField?.isAnnotationPresent(Inject::class.java) == true }
            .forEach { property ->
                val injectAnnotation = property.javaField!!.getAnnotation(Inject::class.java)
                val qualifier = injectAnnotation.qualifier
                injectProperty(target, property, qualifier)
            }
    }

    private fun injectProperty(
        target: Any,
        property: KMutableProperty<*>,
        qualifier: QualifierType
    ) {
        val instance = DIContainer.resolve(property.returnType.classifier as KClass<*>, qualifier)
        property.isAccessible = true
        property.setter.call(target, instance)
    }
}
