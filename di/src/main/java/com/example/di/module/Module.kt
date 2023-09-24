package com.example.di.module

import com.example.di.annotation.FieldInject
import com.example.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

abstract class Module(private val parentModule: Module? = null) {
    private val cache = mutableMapOf<String, Any>()

    protected fun <T : Any> provideInstance(
        clazz: Class<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        if (qualifier != null && qualifier.hasAnnotation<Qualifier>().not()) {
            throw IllegalArgumentException("qualifier 인자는 Qualifier를 메타 애노테이션으로 가져야 합니다")
        }
        val injectableFunctionWithModuleMap = searchInjectableFunctions(clazz, qualifier)
        return when (injectableFunctionWithModuleMap.size) {
            0 -> createWithPrimaryConstructor(clazz)
            1 -> {
                val (function, module) = injectableFunctionWithModuleMap.entries.first()
                module.getOrCreateInstance(function)
            }

            else -> throw IllegalStateException("실행할 함수를 선택할 수 없습니다.")
        }
    }

    protected fun <T : Any> provideInjectField(instance: T) {
        instance::class.declaredMemberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .filter { it.hasAnnotation<FieldInject>() }
            .forEach { field -> instance.injectField(field) }
    }

    private fun <T : Any> searchInjectableFunctions(
        clazz: Class<T>,
        qualifier: KClass<out Annotation>? = null,
    ): Map<KFunction<*>, Module> {
        return getPublicMethodMap()
            .filter { (func, _) -> clazz.kotlin.isSubclassOf(func.returnType.jvmErasure) }
            .filter { (func, _) -> qualifier?.let { hasQualifierAtFunc(func, it) } ?: true }
            .takeUnless { it.isEmpty() }
            ?: parentModule?.searchInjectableFunctions(clazz, qualifier) ?: mapOf()
    }

    private fun hasQualifierAtFunc(func: KFunction<*>, qualifier: KClass<out Annotation>): Boolean {
        val funcQualifiers = func.annotations
            .filter { it.annotationClass.hasAnnotation<Qualifier>() }
            .map { it.annotationClass }
        if (funcQualifiers.contains(qualifier)) return true
        return false
    }

    private fun <T : Any> T.injectField(field: KMutableProperty<*>) {
        if (field.visibility != KVisibility.PUBLIC) throw IllegalStateException("필드 주입을 받으려는 ${field.name}의 가시성이 공개되어 있지 않습니다.")
        val qualifier =
            field.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }
        field.setter.call(
            this,
            provideInstance(field.returnType.jvmErasure.java, qualifier?.annotationClass),
        )
    }

    private fun <T : Any> Module.createWithModuleFunc(func: KFunction<*>): T {
        @Suppress("UNCHECKED_CAST", "LABEL_RESOLVE_WILL_CHANGE")
        return func.call(this@Module, *this@Module.getArguments(func).toTypedArray()) as T
    }

    private fun <T : Any> createWithPrimaryConstructor(clazz: Class<T>): T {
        val primaryConstructor = clazz.kotlin.primaryConstructor
            ?: throw NullPointerException("모듈에 특정 클래스를 주 생성자로 인스턴스화 하는데 필요한 인자를 제공하는 함수를 정의하지 않았습니다")
        val args = this.getArguments(primaryConstructor)
        return primaryConstructor.call(*args.toTypedArray()).apply { provideInjectField(this) }
    }

    private fun Module.getArguments(func: KFunction<*>): List<Any> {
        return func.valueParameters.map { param ->
            val qualifier =
                param.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }
            @Suppress("LABEL_RESOLVE_WILL_CHANGE")
            this@Module.provideInstance(param.type.jvmErasure.java, qualifier?.annotationClass)
        }
    }

    private fun <T : Any> getOrCreateInstance(func: KFunction<*>): T {
        val key = func.name
        if (cache[key] == null) cache[key] = this.createWithModuleFunc(func)
        @Suppress("UNCHECKED_CAST")
        return cache[key] as T
    }

    private fun getPublicMethodMap(): Map<KFunction<*>, Module> {
        return this@Module::class.declaredMemberFunctions
            .filter { it.visibility == KVisibility.PUBLIC }
            .associateWith { this@Module }
    }
}
