package com.example.di

import androidx.lifecycle.ViewModel
import com.example.di.annotations.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

object DIContainer {
    private val instances = mutableMapOf<Pair<KClass<*>, String?>, Any>()

    fun getInstance(
        type: KClass<*>,
        qualifier: String? = null,
    ): Any = instances[type to qualifier] ?: throw NullPointerException("No Instance Found")

    fun setInstance(
        type: KClass<*>,
        instance: Any,
        qualifier: String? = null,
    ) {
        instances[type to qualifier] = instance
    }

    inline fun <reified T : Any> inject(qualifier: String? = null): T = getInstance(T::class, qualifier) as T

    fun injectFieldDependencies(target: Any) {
        val injectionProperties =
            target::class.declaredMemberProperties.filter {
                it.javaField?.getAnnotation(FieldInject::class.java) != null
            }

        injectionProperties.forEach { property ->
            val qualifier = property.javaField?.getAnnotation(Qualifier::class.java)?.value
            property.isAccessible = true
            val value = getInstance(property.returnType.classifier as KClass<*>, qualifier)
            property.javaField?.set(target, value)
        }
    }

    fun <T : ViewModel> setViewModelInstance(
        type: KClass<T>,
        instance: T,
    ) {
        instances[type to null] = instance
    }
}
