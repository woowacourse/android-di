package com.woowa.di.singleton

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.woowa.di.component.Component
import com.woowa.di.findQualifierClassOrNull
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.jvmErasure

class SingletonComponent<binder : Any> private constructor(private val binderClazz: KClass<binder>) :
    Component, DefaultLifecycleObserver {
        override val parent: KClass<out Component>? = null

        private lateinit var binderInstance: binder
        private lateinit var diInstances: Map<String, Any?>

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            binderInstance = binderClazz.createInstance()
            diInstances = createDIInstance()
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            instances.remove(binderClazz)
            owner.lifecycle.removeObserver(this)
        }

        private fun createDIInstance(): Map<String, Any?> {
            return binderClazz.declaredMemberFunctions.filter { it.visibility == KVisibility.PUBLIC }
                .associate { kFunc ->
                    val key =
                        kFunc.returnType.jvmErasure.simpleName
                            ?: error("익명 객체와 같이, 이름이 없는 객체는 di 주입을 할 수 없습니다.")
                    val result = kFunc.call(binderInstance)

                    kFunc.findQualifierClassOrNull()?.let { qualifier ->
                        return@associate (key + qualifier.simpleName) to result
                    }

                    return@associate key to result
                }
        }

        override fun getDIInstance(
            type: KClass<*>,
            qualifier: KClass<out Annotation>?,
        ): Any? {
            qualifier?.let {
                return diInstances[(type.simpleName + qualifier.simpleName)]
            }
            return diInstances[type.simpleName]
        }

        companion object {
            private val instances = mutableMapOf<KClass<*>, SingletonComponent<*>>()

            fun <binder : Any> getInstance(binderClazz: KClass<binder>): SingletonComponent<binder> {
                return instances.getOrPut(binderClazz) {
                    val newInstance = SingletonComponent(binderClazz)
                    ProcessLifecycleOwner.get().lifecycle.addObserver(newInstance)
                    newInstance
                } as SingletonComponent<binder>
            }
        }
    }
