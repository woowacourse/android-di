package com.woowa.di.component

import android.content.Context
import com.woowa.di.singleton.SingletonComponent
import com.woowa.di.singleton.SingletonComponentManager
import com.woowa.di.viewmodel.ViewModelComponent
import com.woowa.di.viewmodel.ViewModelComponentManager
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

fun injectDI(
    applicationContext: Context,
    block: DIBuilder.() -> Unit,
) {
    DIBuilder().initApplicationContext(applicationContext).apply(block)
}

class DIBuilder {
    fun initApplicationContext(context: Context): DIBuilder {
        applicationContext = context
        return this
    }

    fun binder(binder: KClass<*>) {
        val componentClazz =
            binder.findAnnotation<InstallIn>()?.component
                ?: error("InstallIn 어노테이션을 통해 component를 명시해주세요")
        when (componentClazz) {
            ViewModelComponent::class -> ViewModelComponentManager.registerBinder(binder)
            SingletonComponent::class -> SingletonComponentManager.registerBinder(binder)
        }
    }

    companion object {
        lateinit var applicationContext: Context
    }
}
