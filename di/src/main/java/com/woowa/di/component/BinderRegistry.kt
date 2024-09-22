package com.woowa.di.component

import android.app.Application
import android.content.Context
import com.woowa.di.activity.ActivityComponent
import com.woowa.di.activity.ActivityComponentManager
import com.woowa.di.activity.ActivityLifecycleListener
import com.woowa.di.singleton.SingletonComponent2
import com.woowa.di.singleton.SingletonComponentManager2
import com.woowa.di.viewmodel.ViewModelComponent2
import com.woowa.di.viewmodel.ViewModelComponentManager2
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

fun injectDI(
    app: Application,
    block: DIBuilder.() -> Unit,
) {
    DIBuilder().initApplicationContext(app.applicationContext).apply(block).createSingleton(app)
    app.registerActivityLifecycleCallbacks(ActivityLifecycleListener())
}

class DIBuilder {
    fun initApplicationContext(context: Context): DIBuilder {
        applicationContext = context
        return this
    }

    fun createSingleton(app:Application):DIBuilder {
        SingletonComponentManager2.createComponent(app::class)
        return this
    }

    fun binder(binder: KClass<*>) {
        val componentClazz =
            binder.findAnnotation<InstallIn>()?.component2
                ?: error("InstallIn 어노테이션을 통해 component를 명시해주세요")
        when (componentClazz) {
            ViewModelComponent2::class -> ViewModelComponentManager2.registerBinder(binder)
            ActivityComponent::class -> ActivityComponentManager.registerBinder(binder)
            SingletonComponent2::class -> SingletonComponentManager2.registerBinder(binder)
        }
    }

    companion object {
        lateinit var applicationContext: Context
    }
}
