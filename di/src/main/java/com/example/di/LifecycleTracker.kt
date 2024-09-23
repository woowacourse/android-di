package com.example.di

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.di.annotation.lifecycle.LifeCycle
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

class LifecycleTracker(private val clazz: Any) : DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {
        findInjectedFields(clazz).forEach { property ->
            Injector.deleteInstance(property::class)
        }
        super.onDestroy(owner)
    }

    private fun <T : Any> findInjectedFields(target: T): List<KMutableProperty1<out T, *>> {
        return target::class.declaredMemberProperties
            .filter { kProperty ->
                hasLifecycleAnnotation(kProperty)
            }.map { kProperty ->
                kProperty as KMutableProperty1<out T, *>
            }
    }

    private fun <T : Any> hasLifecycleAnnotation(kProperty: KProperty1<out T, *>) =
        kProperty.annotations.firstOrNull { annotation ->
            annotation.annotationClass.hasAnnotation<LifeCycle>()
        } != null
}
