package com.woowa.di.activity

import androidx.activity.ComponentActivity
import com.woowa.di.component.Component
import com.woowa.di.component.ComponentManager
import com.woowa.di.component.ParentManager
import com.woowa.di.singleton.SingletonComponentManager
import kotlin.reflect.KClass

@ParentManager(SingletonComponentManager::class)
object ActivityRetainedComponentManager : ComponentManager() {
    override fun <T : Any> getComponentInstance(componentType: KClass<out T>): Component =
        ActivityRetainedComponent.getInstance(componentType as KClass<out ComponentActivity>)
}
