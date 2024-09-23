package com.woowa.di.singleton

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.woowa.di.ApplicationContext
import com.woowa.di.component.Component
import com.woowa.di.component.DIBuilder
import com.woowa.di.findQualifierClassOrNull
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

class SingletonComponent<T : Application> private constructor(private val clazz: KClass<T>) :
    Component, DefaultLifecycleObserver {
    private val diFunc: MutableMap<KFunction<*>, Any> = mutableMapOf()
    private val diInstances: MutableMap<String, Any?> = mutableMapOf()

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        diInstances.clear()
        instance = null
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
            injectSingletonComponentFields(it)
        }
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
        private var instance: SingletonComponent<*>? = null

        fun <binder : Application> getInstance(binderClazz: KClass<binder>): SingletonComponent<binder> {
            return instance as? SingletonComponent<binder> ?: initInstance(binderClazz)
        }

        private fun <binder : Application> initInstance(binderClazz: KClass<binder>): SingletonComponent<binder> {
            val newInstance = SingletonComponent(binderClazz)
            instance = newInstance
            ProcessLifecycleOwner.get().lifecycle.addObserver(newInstance)
            return newInstance
        }
    }
}
