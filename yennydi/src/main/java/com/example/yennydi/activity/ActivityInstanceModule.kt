package com.example.yennydi.activity

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.di.DependencyContainer
import kotlin.reflect.KClass

class ActivityInstanceModule() : DependencyContainer, DefaultLifecycleObserver {
    private val instances = hashMapOf<KClass<out Any>, Any>()

    override fun <T : Any> getInstance(
        kClass: KClass<*>,
        annotation: Annotation?,
    ): T? = instances[kClass] as? T

    override fun <T : Any> add(instance: T) {
        instances[instance::class] = instance
    }

    override fun clear() {
        instances.clear()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        clear()
        super.onDestroy(owner)
    }
}
