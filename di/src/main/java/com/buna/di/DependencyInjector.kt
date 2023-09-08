package com.buna.di

import com.buna.di.annotation.Inject
import com.buna.di.module.Module
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure

data class Qualify(
    val module: Module,
    val type: KType,
    val annotation: Annotation? = null,
)

object DependencyInjector {
    private val providers = mutableMapOf<Qualify, KFunction<*>>()
    private val typeMatches = mutableMapOf<KType, KType>()
    private val cache = mutableMapOf<Qualify, Any>()

    fun <T : Any> inject(clazz: KClass<T>): T {
        val primaryConstructor =
            clazz.primaryConstructor ?: throw IllegalArgumentException("주생성자 없음")
        val parameters = primaryConstructor.parameters
        val arguments = parameters.map { parameter ->
            val hasQualifiedAnnotation =
                parameter.annotations.any(DependencyInjector::isAnnotationPresent)
            val isSameType = isSameType(parameter.type)

            if (hasQualifiedAnnotation && isSameType) {
                val qualify = providers.keys.find { qualify ->
                    qualify.annotation == parameter.annotations.first() &&
                        qualify.type == parameter.type
                }
                val cached = cache[qualify]
                if (cached != null) {
                    return@map cached
                }
                val instance = providers[qualify]?.call(qualify!!.module)
                cache[qualify!!] = instance!!
                instance
            } else if (isSameType) {
                val qualify = providers.keys.find { qualify ->
                    qualify.type == parameter.type
                }
                val cached = cache[qualify]
                if (cached != null) {
                    return@map cached
                }
                val instance = providers[qualify]?.call(qualify!!.module)
                    ?: throw IllegalArgumentException("의존성 주입 실패")
                cache[qualify!!] = instance
                instance
            } else {
                val childType =
                    typeMatches[parameter.type] ?: throw IllegalArgumentException("의존성 주입 실패")
                inject(childType.classifier as KClass<*>)
            }
        }

        val instance = primaryConstructor.call(*arguments.toTypedArray())
        clazz.java.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true
                field.set(
                    instance,
                    inject(typeMatches[field.type.kotlin.starProjectedType]!!.jvmErasure),
                )
            }
        }

        return instance
    }

    private fun isAnnotationPresent(annotation: Annotation): Boolean {
        return providers.keys.any { qualify -> qualify.annotation == annotation }
    }

    private fun isSameType(type: KType): Boolean {
        return providers.keys.any { qualify -> qualify.type == type }
    }

    fun addTypeMatch(parentType: KType, childType: KType) {
        typeMatches[parentType] = childType
    }

    fun addProvider(module: Module, function: KFunction<*>) {
        val qualify = Qualify(module, function.returnType, function.annotations.firstOrNull())
        providers[qualify] = function
    }
}

fun modules(vararg modules: Module) {
    modules.forEach { module ->
        val providers = module::class.declaredMemberFunctions
        providers.forEach { provider -> DependencyInjector.addProvider(module, provider) }
    }
}

fun matchTypes(vararg classes: Pair<KClass<*>, KClass<*>>) {
    classes.forEach { (parentClass, childClass) ->
        val parentType = parentClass.starProjectedType
        val childType = childClass.starProjectedType

        DependencyInjector.addTypeMatch(parentType, childType)
    }
}
