package com.created.customdi

import android.util.Log
import com.created.customdi.DiContainer.modules
import com.created.customdi.annotation.Field
import com.created.customdi.annotation.Qualifier
import com.created.customdi.annotation.Singleton
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

object Injector {
    const val INVALID_CLASS_TYPE = "[ERROR] Its class type cannot be injected"
    private const val INVALID_KEY = "[ERROR] Cannot find value with a matching key"
    private const val INVALID_FUNCTION = "[ERROR] Function not found in any module"

    inline fun <reified T : Any> inject(): T {
        val clazz = T::class
        val constructors =
            clazz.primaryConstructor ?: throw IllegalStateException(INVALID_CLASS_TYPE)

        val instances = constructors.parameters.map { kParameter ->
            kParameter.getSingletonIfInstantiated() ?: kParameter.instantiate()
        }

        return constructors.call(*instances.toTypedArray()).apply {
            injectField(this)
        }
    }

    inline fun <reified T : Any> injectField(clazz: T) {
        val properties = T::class.declaredMemberProperties.filter { property ->
            property.hasAnnotation<Field>()
        }

        if (properties.isEmpty()) return

        properties.forEach { property ->
            Log.d("12312311", property.toString())
            Log.d("12312322", property.parameters.toString())
            val instance = property.getSingletonIfInstantiated()
                ?: property.instantiate()
            Log.d("12312344", property.toString())
            property.isAccessible = true
            property.javaField?.set(clazz, instance)
        }
    }

    fun Any.getSingletonIfInstantiated(): Any? {
        val type = when (this) {
            is KProperty1<*, *> -> returnType.jvmErasure
            is KParameter -> type.jvmErasure
            else -> throw IllegalArgumentException()
        }

        return DiContainer.singletonInstance[type]
    }

    fun KProperty1<*, *>.instantiate(): Any {
        val func = when (annotations.any { it.hasQualifier() }) {
            true -> DiContainer.qualifiedInstance[annotations.filter { it.hasQualifier() }]
            false -> DiContainer.instance[returnType]
        } ?: throw IllegalArgumentException(INVALID_KEY)

        val module = modules.find { module ->
            module::class.declaredFunctions.any {
                if (func.annotations.any { it.hasQualifier() }) {
                    it.annotations.filter { it.hasQualifier() } == func.annotations.filter { it.hasQualifier() }
                } else {
                    it.returnType.jvmErasure == func.returnType.jvmErasure
                }
            }
        } ?: throw IllegalArgumentException(INVALID_FUNCTION)

        val arg = func.valueParameters.takeIf { it.isNotEmpty() }?.map {
            it.instantiate()
        }?.toTypedArray() ?: emptyArray()

        return (func.call(module, *arg) ?: throw IllegalArgumentException()).also {
            it.toSingletonIfSingleton(func)
        }
    }

    fun KParameter.instantiate(): Any {
        val func = when (annotations.any { it.hasQualifier() }) {
            true -> DiContainer.qualifiedInstance[annotations.filter { it.hasQualifier() }]
            false -> DiContainer.instance[type]
        } ?: throw IllegalArgumentException(INVALID_KEY)

        val module = modules.find { module ->
            module::class.declaredFunctions.any {
                if (func.annotations.any { it.hasQualifier() }) {
                    it.annotations.filter { it.hasQualifier() } == func.annotations.filter { it.hasQualifier() }
                } else {
                    it.returnType.jvmErasure == func.returnType.jvmErasure
                }
            }
        } ?: throw IllegalArgumentException(INVALID_FUNCTION)

        val arg = func.valueParameters.takeIf { it.isNotEmpty() }?.map {
            it.instantiate()
        }?.toTypedArray() ?: emptyArray()

        return (func.call(module, *arg) ?: throw IllegalArgumentException()).also {
            it.toSingletonIfSingleton(func)
        }
    }

    private fun Annotation.hasQualifier() = this.annotationClass.hasAnnotation<Qualifier>()

    private fun Any.toSingletonIfSingleton(func: KFunction<*>) {
        if (func.hasAnnotation<Singleton>()) DiContainer.setSingleton(this, this::class)
    }
}
