package com.woowa.di.singleton

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.woowa.di.ApplicationContext
import com.woowa.di.component.Component2
import com.woowa.di.component.DIBuilder
import com.woowa.di.findQualifierClassOrNull
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinProperty

class SingletonComponent2<T : Application> private constructor(private val clazz: KClass<T>) :
    Component2, DefaultLifecycleObserver {
        private val diFunc: MutableMap<KFunction<*>, Any> = mutableMapOf()
        private val diInstances: MutableMap<String, Any?> = mutableMapOf()

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            diInstances.clear()
            instances.remove(clazz)
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
                        it.hasAnnotation<ApplicationContext>() -> DIBuilder.applicationContext
                        else ->
                            SingletonComponentManager2.getDIInstance(
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

            injectFields(instance)
            return instance
        }

        private fun injectFields(instance: Any?) {
            val fields =
                requireNotNull(instance)::class.java.declaredFields.onEach { field ->
                    field.isAccessible = true
                }.filter { it.isAnnotationPresent(Inject::class.java) }

            fields.map { field ->
                val fieldInstance =
                    SingletonComponentManager2.getDIInstance(
                        field.type.kotlin,
                        field.kotlinProperty?.findQualifierClassOrNull(),
                    )
                field.set(instance, fieldInstance)
            }
        }

        override fun deleteDIInstance(
            type: KClass<*>,
            qualifier: KClass<out Annotation>?,
        ) {
            qualifier?.let {
                diInstances.remove(type.simpleName + qualifier.simpleName)
                return
            }
            diInstances.remove(type.simpleName)
        }

        companion object {
            private val instances = mutableMapOf<KClass<*>, SingletonComponent2<*>>()

            fun <binder : Application> getInstance(binderClazz: KClass<binder>): SingletonComponent2<binder> {
                return instances.getOrPut(binderClazz) {
                    val newInstance = SingletonComponent2(binderClazz)
                    instances[binderClazz] = newInstance
                    ProcessLifecycleOwner.get().lifecycle.addObserver(newInstance)
                    newInstance
                } as SingletonComponent2<binder>
            }
        }
    }
