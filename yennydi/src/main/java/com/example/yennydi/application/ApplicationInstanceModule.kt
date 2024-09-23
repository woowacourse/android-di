package com.example.yennydi.application

import androidx.lifecycle.DefaultLifecycleObserver
import com.example.di.DependencyContainer
import com.example.di.DependencyModule
import kotlin.reflect.KClass

class ApplicationInstanceModule() : DependencyContainer, DefaultLifecycleObserver {
    private val instances = hashMapOf<KClass<out Any>, Any>()

    override fun <T : Any> getInstance(
        kClass: KClass<*>,
        annotation: Annotation?,
    ): T? {
        if (annotation == null) return instances[kClass] as? T
        val implements = DependencyModule.getImplementationClass(kClass, annotation)
        return instances[implements] as? T
    }

    override fun <T : Any> add(instance: T) {
        instances[instance::class] = instance
    }

    override fun clear() {
        instances.clear()
    }
}
