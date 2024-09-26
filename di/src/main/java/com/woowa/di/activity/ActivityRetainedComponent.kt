package com.woowa.di.activity

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.woowa.di.ActivityContext
import com.woowa.di.ApplicationContext
import com.woowa.di.component.Component
import com.woowa.di.findQualifierClassOrNull
import com.woowa.di.injectFieldFromComponent
import com.woowa.di.singleton.SingletonComponent
import com.woowa.di.singleton.SingletonComponentManager
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

class ActivityRetainedComponent<T : ComponentActivity> private constructor(private val targetClazz: KClass<T>) :
    Component, DefaultLifecycleObserver {
    private val diFunc: MutableMap<KFunction<*>, Any> = mutableMapOf()
    private val diInstances: MutableMap<String, Any?> = mutableMapOf()
    private var context: Context? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        context = (owner as? ComponentActivity)?.baseContext
            ?: error("ComponentActivity에서만 DI를 주입할 수 있습니다.")
        injectFieldFromComponent<ActivityRetainedComponentManager>(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        context = null
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
                    it.hasAnnotation<ApplicationContext>() -> SingletonComponent.applicationContext
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
            injectFieldFromComponent<ActivityRetainedComponentManager>(it)
        }
        return instance
    }

    companion object {
        private val instances = mutableMapOf<KClass<*>, ActivityRetainedComponent<*>>()

        fun <binder : ComponentActivity> getInstance(binderClazz: KClass<binder>): ActivityRetainedComponent<binder> {
            return instances.getOrPut(binderClazz) {
                val newInstance = ActivityRetainedComponent(binderClazz)
                instances[binderClazz] = newInstance
                newInstance
            } as ActivityRetainedComponent<binder>
        }

        fun deleteInstance(binderClazz: KClass<*>) {
            instances.remove(binderClazz)
        }
    }
}
