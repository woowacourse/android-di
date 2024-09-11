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

class InstanceContainer(
    private val applicationContext: Context,
    modules: List<Any>,
) {
    private val functionsByReturnType: Map<KType, KFunction<*>> =
        modules
            .flatMap { module ->
                module::class.declaredMemberFunctions
            }
            .associateBy(KFunction<*>::returnType)

    private val instances: MutableMap<QualifiedType, Any> = mutableMapOf()

    init {
        // 미리 각 모듈의 인스턴스를 캐시에 저장해야 오류가 나지 않음
        saveModuleInstances(modules)
        functionsByReturnType.forEach { (returnType, function) ->
            resolveInstance(returnType, function.annotations)
        }
    }

    fun <T : Any> instanceOf(kParameter: KParameter): T {
        return instanceOf(kParameter.type)
    }

    fun <T : Any> instanceOf(kClassifier: KClassifier): T {
        return instanceOf(kClassifier.createType())
    }

    fun <T : Any> instanceOf(kCallable: KCallable<*>): T {
        return instanceOf(kCallable.returnType)
    }

    private fun <T : Any> instanceOf(kType: KType): T {
        val qualifierAnnotation = qualifierAnnotationOf(kType)
        val qualifiedType = qualifiedTypeOf(kType, qualifierAnnotation)

        return qualifiedInstanceOf(qualifiedType)
    }

    private fun qualifierAnnotationOf(kAnnotatedElement: KAnnotatedElement): Annotation? =
        kAnnotatedElement.annotations.firstOrNull { annotation ->
            annotation::class.findAnnotation<Qualifier>() != null
        }

    private fun qualifiedTypeOf(kType: KType, qualifierAnnotation: Annotation?): QualifiedType {
        val qualifier = qualifierAnnotation?.annotationClass?.simpleName
        return QualifiedType(kType, qualifier)
    }

    private fun <T : Any> qualifiedInstanceOf(qualifiedType: QualifiedType): T {
        val instance = instances[qualifiedType]
        checkNotNull(instance) { EXCEPTION_NO_MATCHING_PROPERTY.format(qualifiedType.returnType) }
        return instance as T
    }

    private fun resolveInstance(kType: KType, annotations: List<Annotation>): Any {
        val contextAnnotation = annotations.firstOrNull { annotation ->
            annotation.annotationClass == ApplicationContext::class
        }

        if (contextAnnotation != null && kType.classifier == Context::class) {
            return applicationContext
        }

        val qualifierAnnotation = annotations.firstOrNull { annotation ->
            annotation::class.findAnnotation<Qualifier>() != null
        }
        val qualifiedType = QualifiedType(kType, qualifierAnnotation?.annotationClass?.simpleName)
        val existingInstance = instances[qualifiedType]
        if (existingInstance != null) return existingInstance

        val function = functionsByReturnType[kType]
        requireNotNull(function) { EXCEPTION_NO_MATCHING_FUNCTION.format(kType) }

        // 재귀 주입
        val parameterValues = function.parameters.associateWith { parameter ->
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
