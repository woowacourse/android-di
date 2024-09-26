package com.zzang.di

import com.zzang.di.annotation.Inject
import com.zzang.di.annotation.QualifierType
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

object DependencyInjector {
    fun <T : Any> inject(
        targetClass: KClass<T>,
        qualifier: QualifierType? = null,
        owner: Any? = null,
    ): T {
        val instance = DIContainer.resolve(type = targetClass, qualifier = qualifier, owner = owner)
        injectDependencies(instance, owner)
        return instance
    }

    fun <T : Any> injectDependencies(
        target: T,
        owner: Any?,
    ) {
        val kClass = target::class

        kClass.declaredMemberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .filter { property -> property.javaField?.isAnnotationPresent(Inject::class.java) == true }
            .forEach { property ->
                val injectAnnotation = property.javaField!!.getAnnotation(Inject::class.java)
                val qualifier = injectAnnotation?.qualifier
                injectProperty(target, property, qualifier, owner)
            }
    }

    private fun injectProperty(
        target: Any,
        property: KMutableProperty<*>,
        qualifier: QualifierType? = null,
        owner: Any?,
    ) {
        val instance =
            DIContainer.resolve(
                type = property.returnType.classifier as KClass<*>,
                qualifier = qualifier,
                owner = owner,
            )
        property.isAccessible = true
        property.setter.call(target, instance)
    }
}
