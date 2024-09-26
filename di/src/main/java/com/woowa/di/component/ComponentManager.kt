package com.woowa.di.component

import com.woowa.di.findQualifierClassOrNull
import com.woowa.di.singleton.SingletonComponent
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinProperty

abstract class ComponentManager {
    private val _binders = mutableListOf<Any>()
    val binders: List<Any> get() = _binders.toList()

    private val components: MutableMap<String, KClass<*>> = mutableMapOf()

    fun createComponent(targetClass: KClass<*>): Component =
        getComponentInstance(targetClass).apply {
            saveWhereDIFieldCreated(targetClass)
            saveWhereDIConstructorCreated(targetClass)

            if (this is SingletonComponent) {
                createComponentOfAllBinder(targetClass)
            }
        }

    private fun Component.saveWhereDIConstructorCreated(targetClass: KClass<*>) {
        targetClass.primaryConstructor?.parameters?.forEach { kParameter ->
            val type = kParameter.type.jvmErasure
            val qualifier = kParameter.findQualifierClassOrNull()
            saveWhereDIInstanceCreated(targetClass, type, qualifier)
        }
    }

    private fun Component.saveWhereDIFieldCreated(targetClass: KClass<*>) {
        targetClass.java.declaredFields.onEach { it.isAccessible = true }.filter { property ->
            property.isAnnotationPresent(Inject::class.java)
        }.forEach { property ->
            val type =
                property?.kotlinProperty?.returnType?.jvmErasure
                    ?: error("Kotlin으로 나타낼 수 없는 타입은 DI 주입을 할 수 없습니다.")
            val qualifier = property?.kotlinProperty?.findQualifierClassOrNull()

            saveWhereDIInstanceCreated(targetClass, type, qualifier)
        }
    }

    private fun Component.saveWhereDIInstanceCreated(
        targetClass: KClass<*>,
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        require(
            !isAlreadyCreatedDI(type, qualifier) || findComponentType(
                type,
                qualifier,
            ) == targetClass,
        ) {
            "${type.simpleName} 객체는 ${targetClass.simpleName} 에 대해서만 생성할 수 있습니다."
        }
        findKCallableOrNull(type, qualifier)?.let { callable ->
            registerDIInstance(callable.first, callable.second)
            qualifier?.let {
                components[type.simpleName + it.simpleName] = targetClass
                return
            }
            components[type.simpleName ?: error("익명 객체와 같이, 이름이 없는 객체는 di 주입을 할 수 없습니다.")] =
                targetClass
        }
    }

    private fun Component.createComponentOfAllBinder(targetClass: KClass<*>) {
        _binders.forEach { binder ->
            binder::class.declaredMemberFunctions.filter { it.visibility == KVisibility.PUBLIC }
                .forEach {
                    this.registerDIInstance(binder, it)
                    val type = it.returnType.jvmErasure
                    val qualifier = it.findQualifierClassOrNull()
                    if (qualifier != null) {
                        components[type.simpleName + qualifier.simpleName] = targetClass
                    } else {
                        components[
                            type.simpleName
                                ?: error("익명 객체와 같이, 이름이 없는 객체는 di 주입을 할 수 없습니다."),
                        ] = targetClass
                    }
                }
        }
    }

    private fun findKCallableOrNull(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Pair<Any, KFunction<*>>? {
        val binder =
            findBinder(type, qualifier) ?: return null

        val kFunc =
            findKFunc(binder, type, qualifier) ?: return null
        return binder to kFunc
    }

    private fun findBinder(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ) = _binders.find { binder ->
        binder::class.declaredMemberFunctions.filter { it.visibility == KVisibility.PUBLIC }
            .any {
                it.returnType.jvmErasure == type &&
                    it.findQualifierClassOrNull() == qualifier
            }
    }

    private fun findKFunc(
        binder: Any,
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ) = binder::class.declaredMemberFunctions.filter { it.visibility == KVisibility.PUBLIC }
        .find {
            it.returnType.jvmErasure == type &&
                it.findQualifierClassOrNull() == qualifier
        }

    abstract fun <T : Any> getComponentInstance(componentType: KClass<out T>): Component

    fun getDIInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any? {
        val componentType =
            findComponentType(type, qualifier) ?: return getParentDIInstance(type, qualifier)

        return getComponentInstance(componentType).getDIInstance(type, qualifier)
    }

    private fun findComponentType(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ) = if (qualifier != null) {
        components[type.simpleName + qualifier.simpleName]
    } else {
        components[type.simpleName]
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

    private fun isAlreadyCreatedDI(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ) = components[(type.simpleName + qualifier?.simpleName)] != null || components[type.simpleName] != null

    fun registerBinder(binderClazz: KClass<*>) {
        _binders.add(binderClazz.objectInstance ?: error("binder를 object로 선언해주세요"))
    }
}
