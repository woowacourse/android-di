package com.example.sh1mj1.component.activityscope

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.sh1mj1.annotation.Qualifier
import kotlin.reflect.KClass

data class ActivityScopeComponent(
    val injectedClass: KClass<*>,
    val instanceProvider: (Context) -> Any?,
    val qualifier: Qualifier? = null,
) : DefaultLifecycleObserver {

    /**
     * Provided instance
     * This instance is provided by instanceProvider
     * and attached to lifecycleOwner.
     * When lifecycleOwner is destroyed, this instance will be removed
     */
    private var providedInstance: Any? = null

    fun attachToLifecycle(
        lifecycleOwner: LifecycleOwner,
    ) {
        lifecycleOwner.lifecycle.addObserver(this)
        providedInstance = instanceProvider(lifecycleOwner as Context)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
//        container.remove(injectedClass)
        // 인스턴스 정리
        providedInstance = null
        owner.lifecycle.removeObserver(this)
    }
}

inline fun <reified T : Any?> activityScopeComponent(
    noinline instanceProvider: (Context) -> T?,
    qualifier: Qualifier? = null,
): ActivityScopeComponent =
    ActivityScopeComponent(
        injectedClass = T::class,
        instanceProvider = instanceProvider,
        qualifier = qualifier,
    )
