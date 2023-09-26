package com.example.bbottodi.di

import com.example.bbottodi.di.annotation.Qualifier
import com.example.bbottodi.di.model.InstanceIdentifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

open class Container(
    private val parentContainer: Container? = null,
    private val module: Module,
) {
    private val instances = mutableMapOf<InstanceIdentifier, Any>()

    fun getInstance(clazz: KClass<*>, annotation: Annotation?): Any? {
        val key = InstanceIdentifier(clazz, annotation)
        val function = module::class.declaredFunctions.firstOrNull {
            key.type == it.returnType.jvmErasure && key.qualifier == it.annotations.firstOrNull { annotation ->
                annotation.annotationClass.hasAnnotation<Qualifier>()
            }
        }
        return if (function != null) {
            var instanceKey: InstanceIdentifier? = instances.keys.firstOrNull {
                key.type == it.type && key.qualifier == it.qualifier
            }
            if (instanceKey == null) {
                val instance = getInstance(function)
                instances[key] = instance
                instanceKey = key
            }
            instances[instanceKey]
        } else {
            parentContainer?.getInstance(clazz, annotation)
        }
    }

    private fun getInstance(kFunction: KFunction<*>): Any {
        val args = kFunction.parameters.mapIndexed { index, parameter ->
            if (index == 0) {
                module
            } else {
                getInstance(
                    parameter.type.jvmErasure,
                    parameter.annotations.firstOrNull { annotation ->
                        annotation.annotationClass.hasAnnotation<Qualifier>()
                    },
                )
            }
        }
        return kFunction.call(*args.toTypedArray())!!
    }
}
