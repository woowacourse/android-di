package com.woowa.di.singleton

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.woowa.di.ApplicationContext
import com.woowa.di.component.Component
import com.woowa.di.component.DIBuilder
import com.woowa.di.findQualifierClassOrNull
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinProperty

class SingletonComponent<binder : Any> private constructor(private val binderClazz: KClass<binder>) :
    Component, DefaultLifecycleObserver {
        private lateinit var binderInstance: binder
        private lateinit var diInstances: Map<String, Any?>

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            binderInstance = binderClazz.createInstance()
            diInstances = saveDIInstance()
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            instances.remove(binderClazz)
            owner.lifecycle.removeObserver(this)
        }

        private fun saveDIInstance(): Map<String, Any?> {
            return binderClazz.declaredMemberFunctions.filter { it.visibility == KVisibility.PUBLIC }
                .associate { kFunc ->
                    val key =
                        kFunc.returnType.jvmErasure.simpleName
                            ?: error("익명 객체와 같이, 이름이 없는 객체는 di 주입을 할 수 없습니다.")

                    val result = createInjectedInstance(kFunc)

                    kFunc.findQualifierClassOrNull()?.let { qualifier ->
                        return@associate (key + qualifier.simpleName) to result
                    }

                    return@associate key to result
                }
        }

        private fun createInjectedInstance(kFunc: KFunction<*>): Any? {
            return if (kFunc.parameters.any { it.hasAnnotation<ApplicationContext>() }) {
                kFunc.call(binderInstance, DIBuilder.applicationContext)
            } else {
                kFunc.call(binderInstance)
            }
        }

        override fun getDIInstance(
            type: KClass<*>,
            qualifier: KClass<out Annotation>?,
        ): Any? {
            val instance =
                if (qualifier != null) {
                    diInstances[(type.simpleName + qualifier.simpleName)]
                } else {
                    diInstances[type.simpleName]
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
                    SingletonComponentManager.getDIInstance(
                        field.type.kotlin,
                        field.kotlinProperty?.findQualifierClassOrNull(),
                    )
                field.set(instance, fieldInstance)
            }
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
