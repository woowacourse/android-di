package com.woowa.di.singleton

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.woowa.di.ApplicationContext
import com.woowa.di.component.Component
import com.woowa.di.findQualifierClassOrNull
import com.woowa.di.injectFieldFromComponent
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

class SingletonComponent private constructor() :
    Component, DefaultLifecycleObserver {
        private val diFunc: MutableMap<KFunction<*>, Any> = mutableMapOf()
        private val diInstances: MutableMap<String, Any?> = mutableMapOf()

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            deleteAllDIInstance()
            instance = null
            _applicationContext = null
            ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        }

        override fun getDIInstance(
            type: KClass<*>,
            qualifier: KClass<out Annotation>?,
        ): Any? {
            qualifier?.let {
                return diInstances.getOrPut((type.simpleName + it.simpleName)) {
                    createDIInstance(type, qualifier)
                }
            }
            return diInstances.getOrPut(
                type.simpleName ?: error("익명 객체와 같이, 이름이 없는 객체는 di 주입을 할 수 없습니다."),
            ) {
                createDIInstance(type)
            }
        }

        override fun registerDIInstance(
            binder: Any,
            kFunc: KFunction<*>,
        ) {
            diFunc[kFunc] = binder
        }

        private fun createDIInstance(
            type: KClass<*>,
            qualifier: KClass<out Annotation>? = null,
        ): Any? {
            val kFunc =
                diFunc.keys.find { it.returnType.jvmErasure == type && it.findQualifierClassOrNull() == qualifier }
                    ?: error("${type.simpleName}이 component에 등록되지 않았습니다.")

            val parameters =
                kFunc.parameters.filter { it.kind == KParameter.Kind.VALUE }.map {
                    when {
                        it.hasAnnotation<ApplicationContext>() -> applicationContext
                        else ->
                            SingletonComponentManager.getDIInstance(
                                it.type.jvmErasure,
                                it.findQualifierClassOrNull(),
                            )
                    }
                }

            val instance =
                if (parameters.isEmpty()) {
                    kFunc.call(diFunc[kFunc])
                } else {
                    kFunc.call(diFunc[kFunc], *parameters.toTypedArray())
                }

            instance?.let {
                injectFieldFromComponent<SingletonComponentManager>(it)
            }
            return instance
        }

        override fun deleteAllDIInstance() {
            diInstances.clear()
        }

        companion object {
            private var _applicationContext: Context? = null
            val applicationContext get() = _applicationContext

            fun initApplicationContext(context: Context) {
                require(_applicationContext == null) {
                    "binder는 단 한 번만 초기화 가능합니다."
                }
                _applicationContext = context
            }

            private var instance: SingletonComponent? = null

            fun getInstance(): SingletonComponent {
                return instance ?: initInstance()
            }

            private fun initInstance(): SingletonComponent {
                val newInstance = SingletonComponent()
                instance = newInstance
                ProcessLifecycleOwner.get().lifecycle.addObserver(newInstance)
                return newInstance
            }
        }
    }
