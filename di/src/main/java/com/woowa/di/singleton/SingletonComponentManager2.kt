package com.woowa.di.singleton

import android.app.Application
import com.woowa.di.component.Component
import com.woowa.di.component.Component2
import com.woowa.di.component.ComponentManager
import com.woowa.di.component.ComponentManager2
import com.woowa.di.component.NoParent
import com.woowa.di.component.NoParent2
import com.woowa.di.component.ParentManager
import com.woowa.di.component.ParentManager2
import kotlin.reflect.KClass

@ParentManager2(NoParent2::class)
object SingletonComponentManager2 : ComponentManager2() {

    override fun <T : Any> getComponentInstance(componentType: KClass<out T>): Component2 =
        SingletonComponent2.getInstance(componentType as KClass<Application>)
}