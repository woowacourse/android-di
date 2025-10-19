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
    private val instances = mutableMapOf<Any, MutableMap<KClass<*>, Any>>()

    init {
        ScopeContainer.setHandler(scopeAnnotation, this)
    }

    override fun <T : Any> getInstance(
        kClass: KClass<T>,
        qualifier: KClass<out Annotation>?,
        savedStateHandle: SavedStateHandle?,
        context: Any?,
    ): T {
        val activity = context as ComponentActivity
        val container =
            instances.getOrPut(activity) {
                addLifecycleObserver(activity)
                mutableMapOf()
            }
        return container.getOrPut(kClass) {
            Log.d("TAG", "ActivityScopeHandler: $instances")
            DependencyInjector.createInstance(kClass, savedStateHandle, DependencyKey(kClass, null))
        } as T
    }

    private fun addLifecycleObserver(activity: ComponentActivity) {
        activity.lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    instances.remove(activity)
                    Log.d("TAG", "ActivityScopeHandler 삭제: $instances")
                }
            },
        )
    }
}
