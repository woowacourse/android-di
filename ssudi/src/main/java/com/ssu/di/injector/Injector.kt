package com.ssu.di.injector

import com.ssu.di.annotation.Injected
import com.ssu.di.annotation.Qualifier
import com.ssu.di.container.DiContainer
import com.ssu.di.module.Module
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class Injector(
    private val container: DiContainer,
) {
    fun addModuleInstances(module: Module) {
        module::class.declaredFunctions.forEach { function ->
            val arguments = getArguments(function, module)
            val instance =
                requireNotNull(function.callBy(arguments)) { "${function.returnType.jvmErasure.simpleName} $ERROR_INVALID_ARGUMENTS" }

            if (function.hasAnnotation<Qualifier>()) {
                container.createInstance(function.findAnnotation<Qualifier>()!!.type, instance)
            } else {
                container.createInstance(function.returnType.jvmErasure, instance)
            }
        }
    }

    fun <T : Any> create(clazz: KClass<T>): T {
        val instance = container.getInstance(clazz)

        if (instance != null) return instance
        return createInstance(clazz)
    }

    private fun <T> createInstance(clazz: KClass<*>): T {
        val constructor = getPrimaryClass(clazz)

        val arguments = getArguments(constructor, null)
        val instance = constructor.callBy(arguments) as T

        injectOnFields(clazz, instance)

        return instance
    }

    private fun getPrimaryClass(
        clazz: KClass<*>,
    ): KFunction<*> {
        val constructor = clazz.constructors.firstOrNull { it.hasAnnotation<Injected>() }
            ?: clazz.primaryConstructor
        return requireNotNull(constructor) { "${clazz.simpleName} $ERROR_NO_CONSTRUCTOR" }
    }

    private fun getArguments(kFunction: KFunction<*>, module: Module?): Map<KParameter, Any?> {
        val parameters = kFunction.parameters

        return parameters.associateWith { parameter ->
            if (module != null && parameter.type.jvmErasure == module::class) {
                module
            } else if (parameter.hasAnnotation<Qualifier>()) {
                val qualifier = parameter.findAnnotation<Qualifier>()!!.type
                requireNotNull(container.getInstance(qualifier)) { "$qualifier $ERROR_NO_FIELD" }
            } else {
                val type = parameter.type.jvmErasure
                requireNotNull(container.getInstance(type)) { "${type.simpleName} $ERROR_NO_FIELD" }
            }
        }
    }

    private fun <T> injectOnFields(clazz: KClass<*>, instance: T) {
        val properties = clazz.declaredMemberProperties
            .filter { it.hasAnnotation<Injected>() }
            .filterIsInstance<KMutableProperty<*>>()

        properties.forEach { property ->
            property.isAccessible = true
            val newInstance = if (property.hasAnnotation<Qualifier>()) {
                val qualifier = property.findAnnotation<Qualifier>()!!.type
                requireNotNull(container.getInstance(qualifier)) { "${clazz.simpleName} $ERROR_NO_FIELD" }
            } else {
                requireNotNull(container.getInstance(property.returnType.jvmErasure)) { "${clazz.simpleName} $ERROR_NO_FIELD" }
            }
            property.setter.call(instance, newInstance)
        }
    }

    companion object {
        private const val ERROR_NO_CONSTRUCTOR = "주생성자가 존재하지 않습니다"
        private const val ERROR_NO_FIELD = "컨테이너에 해당 인스턴스가 존재하지 않습니다"
        private const val ERROR_INVALID_ARGUMENTS = "인자가 잘못 전달되었습니다"
    }
}
