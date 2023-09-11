package com.woosuk.scott_di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

typealias Declaration<T> = () -> T
object DiContainer {
    val singletons: MutableMap<KClass<*>, Any> = HashMap()
    val declarations: MutableMap<KClass<*>, Declaration<Any>> = HashMap()

    val qualifiedSingletons: MutableMap<String, Any> = HashMap()
    val qualifiedDeclarations: MutableMap<String, Declaration<Any>> = HashMap()

    fun loadModule(module: Module) {
        val allFunctions = module::class.declaredMemberFunctions
        loadDeclarations(module, allFunctions)
        loadSingletons(allFunctions)
        loadQualifiedDeclarations(module, allFunctions)
        loadQualifiedSingletons(allFunctions)
    }

    private fun loadDeclarations(module: Module, allFunctions: Collection<KFunction<*>>) {
        val normalFunctions = allFunctions.filter { !it.hasAnnotation<Qualifier>() }
        normalFunctions.forEach {
            declarations[it.returnType.jvmErasure] =
                { it.call(module) ?: throw IllegalStateException("함수 형태가 잘못됐어요") }
        }
    }

    private fun loadSingletons(allFunctions: Collection<KFunction<*>>) {
        val normalSingletonFunctions = allFunctions.filter {
            !it.hasAnnotation<Qualifier>() && it.hasAnnotation<Singleton>()
        }
        normalSingletonFunctions.forEach {
            val kClass = it.returnType.jvmErasure
            singletons[kClass] = declarations[kClass]?.invoke() as Any
        }
    }

    private fun loadQualifiedDeclarations(module: Module, allFunctions: Collection<KFunction<*>>) {
        val qualifiedFunctions = allFunctions.filter { it.hasAnnotation<Qualifier>() }
        qualifiedFunctions.forEach {
            val qualifierName = it.findAnnotation<Qualifier>()?.name.toString()
            if(qualifierName.isBlank()) throw IllegalStateException("Annotation 이름이 비었어요")
            qualifiedDeclarations[qualifierName] =
                { it.call(module) ?: throw IllegalStateException("함수 형태가 잘못됐어요") }
        }
    }

    private fun loadQualifiedSingletons(allFunctions: Collection<KFunction<*>>) {
        val singletonQualifiedFunctions =
            allFunctions.filter { it.hasAnnotation<Singleton>() && it.hasAnnotation<Qualifier>() }
        singletonQualifiedFunctions.forEach {
            val qualifierName = it.findAnnotation<Qualifier>()?.name.toString()
            if(qualifierName.isBlank()) throw IllegalStateException("Annotation 이름이 비었어요")
            qualifiedSingletons[qualifierName] =
                qualifiedDeclarations[qualifierName]?.invoke() as Any
        }
    }
}
