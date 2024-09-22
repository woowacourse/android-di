package com.woowa.di.singleton

import android.app.Application
import com.woowa.di.component.Component
import com.woowa.di.component.ComponentManager
import com.woowa.di.component.NoParent
import com.woowa.di.component.ParentManager2
import kotlin.reflect.KClass

@ParentManager2(NoParent::class)
object SingletonComponentManager : ComponentManager() {
    override fun <T : Any> getComponentInstance(componentType: KClass<out T>): Component =
        SingletonComponent.getInstance(componentType as KClass<Application>)
}
