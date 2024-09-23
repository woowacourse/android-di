package com.woowa.di.component

import android.app.Application
import com.woowa.di.activity.ActivityComponent
import com.woowa.di.activity.ActivityComponentManager
import com.woowa.di.activity.ActivityLifecycleListener
import com.woowa.di.singleton.SingletonComponent
import com.woowa.di.singleton.SingletonComponentManager
import com.woowa.di.singleton.injectSingletonComponentFields
import com.woowa.di.viewmodel.ViewModelComponent
import com.woowa.di.viewmodel.ViewModelComponentManager
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

fun injectDI(
    app: Application,
    block: DIBuilder.() -> Unit,
) {
    SingletonComponent.initApplicationContext(app.applicationContext)
    DIBuilder().apply(block).createSingleton(app)
    app.registerActivityLifecycleCallbacks(ActivityLifecycleListener())
}

class DIBuilder {
    fun createSingleton(app: Application): DIBuilder {
        SingletonComponentManager.createComponent(app::class)
        injectSingletonComponentFields(app)
        return this
    }

    fun binder(binder: KClass<*>) {
        val componentClazz =
            binder.findAnnotation<InstallIn>()?.component
                ?: error("InstallIn 어노테이션을 통해 component를 명시해주세요")
        when (componentClazz) {
            ViewModelComponent::class -> ViewModelComponentManager.registerBinder(binder)
            ActivityComponent::class -> ActivityComponentManager.registerBinder(binder)
            SingletonComponent::class -> SingletonComponentManager.registerBinder(binder)
        }
    }
}
