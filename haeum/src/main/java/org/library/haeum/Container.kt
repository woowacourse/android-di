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
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
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
        targetInstance::class.memberProperties.filter { property ->
            property.annotations.any {
                it.annotationClass == Inject::class
            }
        }.forEach { targetProperty ->
            val targetField = targetProperty.javaField
            targetField?.isAccessible = true
            targetField?.set(
                targetInstance,
                ModuleInjector.container.getKPropertyInstance(
                    targetField.kotlinProperty ?: throw IllegalArgumentException("2222r"),
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
            ?: throw IllegalArgumentException("찾으려는 return type이 없습니다.")
        return instance as T
    }

    fun <T> getKParameterInstance(kParameter: KParameter): T {
        val annotation =
            kParameter.annotations.find { annotation ->
                annotation::class.hasAnnotation<Qualifier>()
            }
        val instance = types[Type(kParameter.type, annotation?.annotationClass?.simpleName)]
            ?: throw IllegalArgumentException("찾으려는 return type이 없습니다.")
        return instance as T
    }

    private fun findInstance(
        kType: KType,
        annotations: List<Annotation>,
    ): Any {
        if (isHaeumContext(kType, annotations)) return context
        return findExistingInstance(kType, annotations) ?: createInstance(kType, annotations)
    }

    private fun findExistingInstance(kType: KType, annotations: List<Annotation>): Any? {
        val qualifierAnnotation = annotations.find {
            it.annotationClass.hasAnnotation<Qualifier>()
        }
        val type = Type(kType, qualifierAnnotation?.annotationClass?.simpleName)
        return types[type]
    }

    private fun createInstance(kType: KType, annotations: List<Annotation>): Any {
        val qualifierAnnotation = annotations.find {
            it.annotationClass.hasAnnotation<Qualifier>()
        }
        val type = Type(kType, qualifierAnnotation?.annotationClass?.simpleName)

        val function = returnTypes[type] ?: throw IllegalArgumentException("5555")

        val parameterValues = function.parameters.associateWith { parameter ->
            findInstance(parameter.type, parameter.annotations)
        }
        val instance = function.callBy(parameterValues) ?: throw IllegalArgumentException("6666")

        types[type] = instance
        return instance
    }

    private fun isHaeumContext(kType: KType, annotations: List<Annotation>): Boolean {
        val contextAnnotation =
            annotations.find { annotation ->
                annotation.annotationClass == HaeumContext::class
            }

        return contextAnnotation != null && kType.classifier == Context::class
    }
}
