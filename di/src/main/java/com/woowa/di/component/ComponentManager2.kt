package com.woowa.di.component

import android.util.Log
import com.woowa.di.findQualifierClassOrNull
import com.woowa.di.singleton.SingletonComponent2
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinProperty

abstract class ComponentManager2 {
    private val _binderClazzs = mutableListOf<KClass<*>>()
    val binderClazzs: List<KClass<*>> get() = _binderClazzs.toList()

    private val binders
        get() = _binderClazzs.map { it.createInstance() }

    private val components: MutableMap<String, KClass<*>> = mutableMapOf()

    fun createComponent(
        targetClass: KClass<*>,
    ): Component2<*> =
        getComponentInstance(targetClass).apply {
            targetClass.java.declaredFields.onEach { it.isAccessible = true }.filter { property ->
                property.isAnnotationPresent(Inject::class.java)
            }.forEach { property ->
                val type = property?.kotlinProperty?.returnType?.jvmErasure
                    ?: error("Kotlin으로 나타낼 수 없는 타입은 DI 주입을 할 수 없습니다.")
                val qualifier = property?.kotlinProperty?.findQualifierClassOrNull()

                require(!isAlreadyCreatedDI(type, qualifier)) {
                    "한 객체는 하나의 ${targetClass} ${type.simpleName}에 대해서만 생성할 수 있습니다."
                }

                findKCallableOrNull(type, qualifier)?.let { callable ->
                    registerDIInstance(callable.first, callable.second)
                    qualifier?.let {
                        components[type.simpleName + it.simpleName] = targetClass
                        return@forEach
                    }
                    components[type.simpleName ?: error("익명 객체와 같이, 이름이 없는 객체는 di 주입을 할 수 없습니다.")] =
                        targetClass
                }

            }

            if (this is SingletonComponent2) {
                binders.forEach { binder ->
                    binder::class.declaredMemberFunctions.forEach {
                        this.registerDIInstance(binder, it)
                        val type = it.returnType.jvmErasure
                        val qualifier = it.findQualifierClassOrNull()
                        if (qualifier != null) {
                            components[type.simpleName + qualifier.simpleName] = targetClass
                        } else {
                            components[type.simpleName
                                ?: error("익명 객체와 같이, 이름이 없는 객체는 di 주입을 할 수 없습니다.")] = targetClass
                        }
                    }

                }


            }
        }

    private fun findKCallableOrNull(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Pair<Any, KFunction<*>>? {
        val binder =
            binders.find { it::class.declaredMemberFunctions.any { it.returnType.jvmErasure == type && it.findQualifierClassOrNull() == qualifier } }
                ?: return null
        val kFunc =
            binder::class.declaredMemberFunctions.find { it.returnType.jvmErasure == type && it.findQualifierClassOrNull() == qualifier }
                ?: return null
        return binder to kFunc
    }

    private fun <T : Any> findInstancesOfType(targetClass: KClass<T>): List<T> {
        return binders.filter { targetClass.isInstance(it) }
            .map { it as T }
    }

    abstract fun <T : Any> getComponentInstance(componentType: KClass<out T>): Component2<out T>


    fun getDIInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any? {
        val componentType = if (qualifier != null) {
            components[type.simpleName + qualifier.simpleName]
        } else {
            components[type.simpleName]
        } ?: return getParentDIInstance(type, qualifier)

        return getComponentInstance(componentType).getDIInstance(type, qualifier)

    }

    private fun getParentDIInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): Any? {
        Log.d("테스트", "${components.keys}")
        require(this::class.findAnnotation<ParentManager2>()?.manager != NoParent2::class) {
            "${type.simpleName}이 binder에 정의되어 있지 않습니다."
        }

        val parentManager =
            this::class.findAnnotation<ParentManager2>()?.manager?.objectInstance
                ?: error("${this::class.simpleName}의 parentManager를 정의해주세요")
        return parentManager.getDIInstance(type, qualifier)
    }

    private fun isAlreadyCreatedDI(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ) = components[(type.simpleName + qualifier?.simpleName)] != null || components[type.simpleName] != null

    fun <binder : Any> registerBinder(binderClazz: KClass<binder>) {
        _binderClazzs.add(binderClazz)
    }
}