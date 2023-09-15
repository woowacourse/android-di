package com.woosuk.scott_di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

typealias Declaration<T> = () -> T

object DiContainer {
    private val providers: Providers = Providers()

    fun loadModule(module: Module) {
        val allFunctions = module::class.declaredMemberFunctions
        initServices(module, allFunctions)
    }

    fun <T : Any> inject(kClazz: KClass<T>): T {
        val instance = injectConstructor(kClazz).apply {
            injectFields(kClazz)
        }
        return instance
    }

    private fun initServices(module: Module, functions: Collection<KFunction<*>>) {
        functions.forEach { function ->
            val provider = Provider(module, function = function)
            providers.addService(provider)
        }
    }

    // 생성자 주입
    private fun <T : Any> injectConstructor(kClazz: KClass<T>): T {
        val primaryConstructor =
            kClazz.primaryConstructor
                ?: throw IllegalStateException("${kClazz.simpleName} 주 생성자가 없어요...ㅠ")

        val constructorParameters = primaryConstructor.parameters

        val instances = constructorParameters.map { parameter ->
            providers.getInstance(parameter.type.jvmErasure, parameter.getQualifierAnnotation())
        }.toTypedArray()
        return primaryConstructor.call(*instances)
    }

    private fun <T : Any> injectFields(instance: T) {
        val properties = instance::class
            .declaredMemberProperties.filter { property ->
                property.hasInjectAnnotation() || property.hasQualifierAnnotation()
            }

        properties.forEach { property ->
            property.isAccessible = true
            property.javaField?.set(
                instance,
                providers.getInstance(
                    property.returnType.jvmErasure,
                    property.getQualifierAnnotation()
                )
            )
        }
    }

    private fun KProperty<*>.hasInjectAnnotation(): Boolean {
        return this.annotations.any { it.annotationClass.hasAnnotation<Inject>() }
    }

    private fun KProperty<*>.hasQualifierAnnotation(): Boolean {
        return this.annotations.any { it.annotationClass.hasAnnotation<Qualifier>() }
    }
}

fun KParameter.getQualifierAnnotation() =
    annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }

fun KProperty<*>.getQualifierAnnotation() =
    annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }

fun KFunction<*>.getQualifierAnnotation() =
    annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }
