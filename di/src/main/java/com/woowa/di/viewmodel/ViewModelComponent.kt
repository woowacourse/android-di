package com.woowa.di.viewmodel

import com.woowa.di.findQualifierClassOrNull
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.jvmErasure

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelScope

class ViewModelComponent<binder : Any> private constructor(private val binderClazz: KClass<binder>) {
    private val binderInstance: binder = binderClazz.createInstance()
    private val diInstances: Map<String, Any?> = createDiInstance()

    private fun createDiInstance(): Map<String, Any?> {
        return binderClazz.declaredMemberFunctions.filter { it.visibility == KVisibility.PUBLIC }
            .associate { kFunc ->
                val result = kFunc.call(binderInstance)
                val key =
                    kFunc.returnType.jvmErasure.simpleName
                        ?: error("익명 객체와 같이, 이름이 없는 객체는 di 주입을 할 수 없습니다.")

                kFunc.findQualifierClassOrNull()?.let { qualifier ->
                    return@associate (key + qualifier.simpleName) to result
                }

                return@associate key to result
            }
    }

    fun getDIInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any? {
        qualifier?.let {
            return diInstances[(it.simpleName + type.simpleName)]
        }
        return diInstances[type.simpleName]
    }

    companion object {
        private val instances = mutableMapOf<KClass<*>, ViewModelComponent<*>>()

        fun <binder : Any> getInstance(binderClazz: KClass<binder>): ViewModelComponent<binder> {
            return instances.getOrPut(binderClazz) {
                ViewModelComponent(binderClazz)
            } as ViewModelComponent<binder>
        }

        fun <binder : Any> deleteInstance(binderClazz: KClass<binder>) {
            instances.remove(binderClazz) ?: error("${binderClazz.simpleName}에 대한 인스턴스가 없습니다.")
        }
    }
}
