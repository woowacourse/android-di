package com.woowa.di.component

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.jvmErasure

abstract class ComponentManager<component : Component> {
    private val diInstances: MutableMap<KClass<*>, KClass<*>> = mutableMapOf()

    fun getComponentType(key: KClass<*>): KClass<*> {
        return diInstances[key]
            ?: error("${key.simpleName}에 해당하는 객체가 binder에 등록되지 않았습니다.")
    }

    fun <binder : Any> registerComponent(binderClazz: KClass<binder>) {
        binderClazz.declaredFunctions.forEach { kFunc ->
            diInstances[kFunc.returnType.jvmErasure] = binderClazz
        }
    }
}
