package com.woowa.di.activity

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.woowa.di.ActivityContext
import com.woowa.di.ApplicationContext
import com.woowa.di.component.Component
import com.woowa.di.component.DIBuilder
import com.woowa.di.findQualifierClassOrNull
import com.woowa.di.singleton.SingletonComponentManager
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

class ActivityComponent<T : ComponentActivity> private constructor(private val clazz: KClass<T>) :
    Component, DefaultLifecycleObserver {
        private val diFunc: MutableMap<KFunction<*>, Any> = mutableMapOf()
        private val diInstances: MutableMap<String, Any?> = mutableMapOf()
        private var context: Context? = null

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            context = (owner as ComponentActivity).baseContext
            injectActivityComponentFields(owner)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            context = null
            deleteAllDIInstance()
            owner.lifecycle.removeObserver(this)
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

        override fun deleteAllDIInstance() {
            diInstances.clear()
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
                        it.hasAnnotation<ApplicationContext>() -> DIBuilder.applicationContext
                        it.hasAnnotation<ActivityContext>() -> context
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
                injectActivityComponentFields(it)
            }
            return instance
        }

        fun deleteAllDIInstance(targetClass: KClass<out Activity>) {
            diInstances.clear()
            instances.remove(targetClass)
        }

        companion object {
            private val instances = mutableMapOf<KClass<*>, ActivityComponent<*>>()

            fun <binder : ComponentActivity> getInstance(binderClazz: KClass<binder>): ActivityComponent<binder> {
                return instances.getOrPut(binderClazz) {
                    val newInstance = ActivityComponent(binderClazz)
                    instances[binderClazz] = newInstance
                    newInstance
                } as ActivityComponent<binder>
            }
        }
    }
