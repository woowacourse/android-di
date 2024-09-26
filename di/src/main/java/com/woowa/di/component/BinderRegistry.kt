package com.woowa.di.component

import android.app.Application
import com.woowa.di.activity.ActivityLifecycleListener
import com.woowa.di.activity.ActivityRetainedComponent
import com.woowa.di.activity.ActivityRetainedComponentManager
import com.woowa.di.injectFieldFromComponent
import com.woowa.di.singleton.SingletonComponent
import com.woowa.di.singleton.SingletonComponentManager
import com.woowa.di.viewmodel.ViewModelComponent
import com.woowa.di.viewmodel.ViewModelComponentManager
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

fun injectDI(
    app: Application,
    block: DIBuilder.() -> Unit,
) {
    SingletonComponent.initApplicationContext(app.applicationContext)
    DIBuilder().apply(block)
    SingletonComponentManager.createComponent(app::class)
    injectFieldFromComponent<SingletonComponentManager>(app)
    app.registerActivityLifecycleCallbacks(ActivityLifecycleListener())
}

class DIBuilder {
    fun binder(binder: KClass<*>) {
        val componentClazz =
            binder.findAnnotation<InstallIn>()?.component
                ?: error("InstallIn 어노테이션을 통해 component를 명시해주세요")
        when (componentClazz) {
            ViewModelComponent::class -> ViewModelComponentManager.registerBinder(binder)
            ActivityRetainedComponent::class -> ActivityRetainedComponentManager.registerBinder(binder)
            SingletonComponent::class -> SingletonComponentManager.registerBinder(binder)
        }
    }
}
