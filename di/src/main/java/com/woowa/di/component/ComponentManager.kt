package com.woowa.di.component

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.jvmErasure

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ParentManager(val manager: KClass<out ComponentManager>)

abstract class ComponentManager {
    private val _binderClazzs = mutableListOf<KClass<*>>()
    val binderClazzs: List<KClass<*>> get() = _binderClazzs.toList()

    abstract fun getComponentInstance(binderType: KClass<*>): Component

    fun getDIInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any? {
        val binderType = getBinderTypeOrNull(type) ?: return getParentDIInstance(type, qualifier)
        return getComponentInstance(binderType).getDIInstance(type, qualifier)
    }

    private fun getParentDIInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): Any? {
        require(this::class.findAnnotation<ParentManager>()?.manager != NoParent::class) {
            "${type.simpleName}이 binder에 정의되어 있지 않습니다."
        }

        val parentManager =
            this::class.findAnnotation<ParentManager>()?.manager?.objectInstance
                ?: error("${this::class.simpleName}의 parentManager를 정의해주세요")
        return parentManager.getDIInstance(type, qualifier)
    }

    /**
     * Returns the instance you want to inject, or `null` if the instance does not exist
     */
    fun getBinderTypeOrNull(key: KClass<*>): KClass<*>? {
        return _binderClazzs.find { it.declaredMemberFunctions.find { it.returnType.jvmErasure == key } != null }
    }

    fun <binder : Any> registerBinder(binderClazz: KClass<binder>) {
        _binderClazzs.add(binderClazz)
    }
}

data object NoParent : ComponentManager() {
    override fun getComponentInstance(binderType: KClass<*>): Component {
        throw IllegalArgumentException()
    }
}
