package com.woowa.di.viewmodel

import com.woowa.di.component.ComponentManager
import com.woowa.di.component.ParentManager
import com.woowa.di.singleton.SingletonComponentManager
import kotlin.reflect.KClass

@ParentManager(SingletonComponentManager::class)
object ViewModelComponentManager : ComponentManager() {
    override fun getComponentInstance(binderType: KClass<*>): ViewModelComponent<*> = ViewModelComponent.getInstance(binderType)

    fun deleteDIInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        val binderType = getBinderTypeOrNull(type) ?: return
        getComponentInstance(binderType).deleteDIInstance(type, qualifier)
    }
}
