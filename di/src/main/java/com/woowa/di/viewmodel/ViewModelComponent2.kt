package com.woowa.di.viewmodel

import com.woowa.di.ApplicationContext
import com.woowa.di.component.Component2
import com.woowa.di.component.DIBuilder
import com.woowa.di.findQualifierClassOrNull
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

class ViewModelComponent2<T : Any> private constructor() :
    Component2 {
        private val diFunc: MutableMap<KFunction<*>, Any> = mutableMapOf()
        private val diInstances: MutableMap<String, Any?> = mutableMapOf()

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
                        else -> ViewModelComponentManager2.getDIInstance(it.type.jvmErasure, it.findQualifierClassOrNull())
                    }
                }

            val receiver = diFunc.getValue(kFunc)
            val instance =
                if (parameters.isEmpty()) {
                    kFunc.call(receiver)
                } else {
                    kFunc.call(diFunc[kFunc], parameters.toTypedArray())
                }

            injectViewModelComponentFields(instance)
            return instance
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
            private val instances = mutableMapOf<KClass<*>, ViewModelComponent2<*>>()

            fun <binder : Any> getInstance(binderClazz: KClass<binder>): ViewModelComponent2<binder> {
                return instances.getOrPut(binderClazz) {
                    val newInstance = ViewModelComponent2<binder>()
                    instances[binderClazz] = newInstance
                    newInstance
                } as ViewModelComponent2<binder>
            }

            fun deleteInstance(binderClazz: KClass<*>) {
                instances.remove(binderClazz)
            }
        }
    }
