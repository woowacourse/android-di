package com.woowa.di.viewmodel

import androidx.lifecycle.ViewModel
import com.woowa.di.component.Component
import com.woowa.di.component.ComponentManager
import com.woowa.di.component.ParentManager2
import com.woowa.di.singleton.SingletonComponentManager
import kotlin.reflect.KClass

@ParentManager2(SingletonComponentManager::class)
object ViewModelComponentManager : ComponentManager() {
    override fun <T : Any> getComponentInstance(componentType: KClass<out T>): Component =
        ViewModelComponent.getInstance(componentType as KClass<out ViewModel>)
}
