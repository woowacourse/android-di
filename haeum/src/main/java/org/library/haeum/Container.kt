package org.library.haeum

import android.content.Context
import javax.inject.Qualifier
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.kotlinProperty

class Container(
    private val context: Any,
    modules: List<Any>,
) {
    private val types: HashMap<Type, Any> = hashMapOf()
    private val returnTypes: Map<Type, KFunction<*>> =
        modules.flatMap { module ->
            module::class.declaredMemberFunctions
        }.associateBy { declaredMemberFunction ->
            Type(
                declaredMemberFunction.returnType,
                declaredMemberFunction.annotations.find {
                    it.annotationClass.hasAnnotation<Qualifier>()
                }?.annotationClass?.simpleName,
            )
        }

    init {
        modules.forEach { module ->
            types[Type(module::class.createType(nullable = false))] = module
        }
        returnTypes.forEach { (type, function) ->
            findInstance(type.returnType, function.annotations)
        }
    }

    fun <T : Any> injectTo(targetInstance: T) {
        // @Inject 있는 것만 필터링
        targetInstance::class.java.declaredFields.filter { field ->
            field.annotations.any {
                it.annotationClass == Inject::class
            }
        }.forEach { targetField ->
            targetField.isAccessible = true
            targetField.set(
                targetInstance,
                ModuleInjector.container.getKPropertyInstance(
                    targetField.kotlinProperty ?: throw IllegalArgumentException("2222"),
                ),
            )
        }
    }

    fun <T> getKPropertyInstance(kProperty: KProperty<*>): T {
        val annotation =
            kProperty.annotations.find { annotation ->
                annotation.annotationClass.hasAnnotation<Qualifier>()
            }
        val instance = types[Type(kProperty.returnType, annotation?.annotationClass?.simpleName)]
        return instance as T
    }

    fun <T> getKParameterInstance(kParameter: KParameter): T {
        val annotation =
            kParameter.annotations.find { annotation ->
                annotation::class.hasAnnotation<Qualifier>()
            }
        val instance = types[Type(kParameter.type, annotation?.annotationClass?.simpleName)]
        return instance as T
    }

    private fun findInstance(
        kType: KType,
        annotations: List<Annotation>,
    ): Any {
        val contextAnnotation =
            annotations.find { annotation ->
                annotation.annotationClass == HaeumContext::class
            }

        if (contextAnnotation != null && kType.classifier == Context::class) {
            return context
        }

        val qualifierAnnotation =
            annotations.find { annotation ->
                annotation.annotationClass.hasAnnotation<Qualifier>()
            }
        val type = Type(kType, qualifierAnnotation?.annotationClass?.simpleName)
        val existingInstance = types[type]
        if (existingInstance != null) return existingInstance

        val function = returnTypes[type] ?: throw IllegalArgumentException("5555")

        val parameterValues =
            function.parameters.associateWith { parameter ->
                findInstance(parameter.type, parameter.annotations)
            }
        val instance = function.callBy(parameterValues) ?: throw IllegalArgumentException("6666")

        types[type] = instance
        return instance
    }
}