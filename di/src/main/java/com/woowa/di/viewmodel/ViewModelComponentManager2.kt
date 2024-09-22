package com.woowa.di.viewmodel

import androidx.lifecycle.ViewModel
import com.woowa.di.component.Component2
import com.woowa.di.component.ComponentManager2
import com.woowa.di.component.ParentManager
import com.woowa.di.component.ParentManager2
import com.woowa.di.singleton.SingletonComponentManager
import com.woowa.di.singleton.SingletonComponentManager2
import kotlin.reflect.KClass

@ParentManager2(SingletonComponentManager2::class)
object ViewModelComponentManager2: ComponentManager2() {
    override fun <T : Any> getComponentInstance(componentType: KClass<out T>): Component2<T> =
        ViewModelComponent2.getInstance(componentType as KClass<out ViewModel>) as Component2<T>

}