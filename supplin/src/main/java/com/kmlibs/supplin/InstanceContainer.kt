package com.kmlibs.supplin

import android.content.Context
import com.kmlibs.supplin.annotations.ApplicationContext
import com.kmlibs.supplin.model.QualifiedType
import javax.inject.Qualifier
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KCallable
import kotlin.reflect.KClassifier
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberFunctions

class InstanceContainer(
    private val applicationContext: Context,
    modules: List<Any>,
) {
    private val qualifiedFunctions: Map<QualifiedType, KFunction<*>> =
        modules
            .flatMap { module ->
                module::class.declaredMemberFunctions
            }.associateBy { function ->
                QualifiedType(function.returnType, function.annotations.firstOrNull { annotation ->
                    annotation.annotationClass.hasAnnotation<Qualifier>()
                }?.annotationClass?.simpleName)
            }

    private val instances: MutableMap<QualifiedType, Any> = mutableMapOf()

    init {
        saveModuleInstances(modules)
        qualifiedFunctions.forEach { (qualifiedType, function) ->
            resolveInstance(qualifiedType.returnType, function.annotations)
        }
    }

    fun <T : Any> instanceOf(kParameter: KParameter): T {
        val annotation = kParameter.annotations.firstOrNull { annotation ->
            annotation::class.hasAnnotation<Qualifier>()
        }
        return qualifiedInstanceOf(QualifiedType(kParameter.type, annotation?.annotationClass?.simpleName))
    }

    fun <T : Any> instanceOf(kClassifier: KClassifier): T {
        return instanceOf(kClassifier.createType())
    }

    fun <T : Any> instanceOf(kCallable: KCallable<*>): T {
        val annotation = kCallable.annotations.firstOrNull { annotation ->
            annotation.annotationClass.hasAnnotation<Qualifier>()
        }
        return qualifiedInstanceOf(QualifiedType(kCallable.returnType, annotation?.annotationClass?.simpleName))
    }

    private fun <T : Any> instanceOf(kType: KType): T {
        val qualifierAnnotation = qualifierAnnotationOf(kType)
        val qualifiedType = qualifiedTypeOf(kType, qualifierAnnotation)

        return qualifiedInstanceOf(qualifiedType)
    }

    private fun qualifierAnnotationOf(kAnnotatedElement: KAnnotatedElement): Annotation? =
        kAnnotatedElement.annotations.firstOrNull { annotation ->
            annotation.annotationClass.hasAnnotation<Qualifier>()
        }

    private fun qualifiedTypeOf(
        kType: KType,
        qualifierAnnotation: Annotation?,
    ): QualifiedType {
        val qualifier = qualifierAnnotation?.annotationClass?.simpleName
        return QualifiedType(kType, qualifier)
    }

    private fun <T : Any> qualifiedInstanceOf(qualifiedType: QualifiedType): T {
        val instance = instances[qualifiedType]
        checkNotNull(instance) { EXCEPTION_NO_MATCHING_PROPERTY.format(qualifiedType.returnType) }
        return instance as T
    }

    // TODO 함수 작은 단위로 분리?
    private fun resolveInstance(
        returnType: KType,
        annotations: List<Annotation>,
    ): Any {
        val contextAnnotation =
            annotations.firstOrNull { annotation ->
                annotation.annotationClass == ApplicationContext::class
            }

        if (contextAnnotation != null && returnType.classifier == Context::class) {
            return applicationContext
        }

        val qualifierAnnotation =
            annotations.firstOrNull { annotation ->
                annotation.annotationClass.hasAnnotation<Qualifier>()
            }
        val qualifiedType = QualifiedType(returnType, qualifierAnnotation?.annotationClass?.simpleName)

        val existingInstance = instances[qualifiedType]
        if (existingInstance != null) return existingInstance

        val function = qualifiedFunctions[qualifiedType]
        requireNotNull(function) { EXCEPTION_NO_MATCHING_FUNCTION.format(qualifiedType.returnType) }

        // 재귀 주입
        val parameterValues =
            function.parameters.associateWith { parameter ->
                resolveInstance(parameter.type, parameter.annotations)
            }
        val instance = function.callBy(parameterValues)
        checkNotNull(instance) { EXCEPTION_NULL_INSTANCE.format(function.name) }

        instances[qualifiedType] = instance
        return instance
    }

    private fun saveModuleInstances(modules: List<Any>) {
        modules.forEach { module ->
            instances[QualifiedType(module::class.createType())] = module
        }
    }

    companion object {
        private const val EXCEPTION_NO_MATCHING_PROPERTY =
            "No matching property found for parameter %s"
        private const val EXCEPTION_NO_MATCHING_FUNCTION =
            "No function found for return type: %s"
        private const val EXCEPTION_NULL_INSTANCE =
            "Failed to create instance for the function: %s"
    }
}
