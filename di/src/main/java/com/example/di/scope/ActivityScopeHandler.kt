package com.example.di.scope

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.example.di.DependencyInjector
import com.example.di.DependencyKey
import kotlin.reflect.KClass

object ActivityScopeHandler : ScopeHandler {
    override val scopeAnnotation: KClass<out Annotation> = ActivityScope::class
    private val instances = mutableMapOf<DependencyKey<*>, Any>()

    init {
        ScopeContainer.setHandler(scopeAnnotation, this)
    }

    override fun <T : Any> getInstance(
        kClass: KClass<T>,
        qualifier: KClass<out Annotation>?,
        savedStateHandle: SavedStateHandle?,
        context: Any?,
        hasScope: Boolean,
    ): T {
        val activity = context as ComponentActivity
        val key = DependencyKey(kClass, qualifier)
        val container =
            instances.getOrPut(key) {
                addLifecycleObserver(activity, key)
                DependencyInjector.createInstance(
                    kClass,
                    savedStateHandle,
                    key,
                )
            }
        return container as T
    }

    private fun addLifecycleObserver(
        activity: ComponentActivity,
        key: DependencyKey<*>,
    ) {
        activity.lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    instances.remove(key)
                    Log.d("TAG", "ActivityScopeHandler 삭제: $instances")
                }
            },
        )
    }
}
