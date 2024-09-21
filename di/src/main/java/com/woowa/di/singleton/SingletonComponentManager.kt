package com.woowa.di.singleton

import com.woowa.di.component.Component
import com.woowa.di.component.ComponentManager
import com.woowa.di.component.NoParent
import com.woowa.di.component.ParentManager
import kotlin.reflect.KClass

@ParentManager(NoParent::class)
object SingletonComponentManager : ComponentManager() {
    override fun getComponentInstance(binderType: KClass<*>): Component = SingletonComponent.getInstance(binderType)
}
