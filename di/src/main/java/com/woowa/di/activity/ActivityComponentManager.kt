package com.woowa.di.activity

import androidx.activity.ComponentActivity
import com.woowa.di.component.Component2
import com.woowa.di.component.ComponentManager2
import com.woowa.di.component.ParentManager2
import com.woowa.di.singleton.SingletonComponentManager2
import kotlin.reflect.KClass

@ParentManager2(SingletonComponentManager2::class)
object ActivityComponentManager : ComponentManager2() {
    override fun <T : Any> getComponentInstance(componentType: KClass<out T>): Component2 =
        ActivityComponent.getInstance(componentType as KClass<out ComponentActivity>) as Component2
}
