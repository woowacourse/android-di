package com.woowa.di.activity

import androidx.activity.ComponentActivity
import com.woowa.di.component.Component
import com.woowa.di.component.ComponentManager
import com.woowa.di.component.ParentManager2
import com.woowa.di.singleton.SingletonComponentManager
import kotlin.reflect.KClass

@ParentManager2(SingletonComponentManager::class)
object ActivityComponentManager : ComponentManager() {
    override fun <T : Any> getComponentInstance(componentType: KClass<out T>): Component =
        ActivityComponent.getInstance(componentType as KClass<out ComponentActivity>)
}
