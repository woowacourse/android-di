package com.woowa.di.viewmodel

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.jvmErasure

object ViewModelComponentManager {
    private val diInstances = mutableMapOf<KClass<*>, KClass<*>>()

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
