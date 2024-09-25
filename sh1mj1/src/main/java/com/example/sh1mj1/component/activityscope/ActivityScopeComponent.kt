package com.example.sh1mj1.component.activityscope

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.container.activityscope.DefaultActivityComponentContainer
import kotlin.reflect.KClass

data class ActivityScopeComponent(
    val injectedClass: KClass<*>,
    val instanceProvider: (Context) -> Any,
    val qualifier: Qualifier? = null,
) : DefaultLifecycleObserver {

    private val container: DefaultActivityComponentContainer = DefaultActivityComponentContainer.instance()

    fun attachToLifecycle(
        lifecycleOwner: LifecycleOwner,
    ) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        container. remove(injectedClass)
        owner.lifecycle.removeObserver(this)
    }
}

inline fun <reified T : Any> activityScopeComponent(
    noinline instanceProvider: (Context) -> T,
    qualifier: Qualifier? = null,
): ActivityScopeComponent =
    ActivityScopeComponent(
        injectedClass = T::class,
        instanceProvider = instanceProvider,
        qualifier = qualifier,
    )
