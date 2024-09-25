package com.example.sh1mj1.container.activityscope

import android.content.Context
import com.example.sh1mj1.component.activityscope.ActivityScopeComponent
import kotlin.reflect.KClass

class DefaultActivityComponentContainer private constructor() : ActivityComponentContainer {
    private val components = mutableListOf<ActivityScopeComponent>()

    fun add(component: ActivityScopeComponent) {
        components.add(component)
    }

    fun <T : Any> find(
        injectedClass: KClass<T>,
        context: Context,
    ): T? {
        val component = components.firstOrNull { it.injectedClass == injectedClass }
        println("$TAG components: $components")
        return component?.instanceProvider?.invoke(context) as? T
    }

    fun remove(injectedClass: KClass<*>) {
        components.removeIf { it.injectedClass == injectedClass }
    }

    companion object {
        private var instance: DefaultActivityComponentContainer? = null

        fun instance(): DefaultActivityComponentContainer {
            if (instance == null) {
                instance = DefaultActivityComponentContainer()
            }
            return instance!!
        }
    }
}

interface ActivityComponentContainer

private const val TAG = "DefaultActivityComponentContainer"