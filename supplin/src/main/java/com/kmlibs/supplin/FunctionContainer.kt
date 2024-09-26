package com.kmlibs.supplin

import com.kmlibs.supplin.annotations.Abstract
import com.kmlibs.supplin.annotations.Concrete
import com.kmlibs.supplin.annotations.Supply
import com.kmlibs.supplin.model.QualifiedType
import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

class FunctionContainer(vararg modules: KClass<*>) {
    val qualifiedFunctions: Map<QualifiedType, KFunction<*>> =
        modules.flatMap { module ->
            module.declaredMemberFunctions
                .map { memberFunction ->
                    functionByQualifiedType(memberFunction)
                }
        }.toMap()

    private fun functionByQualifiedType(function: KFunction<*>): Pair<QualifiedType, KFunction<*>> =
        when {
            function.hasAnnotation<Abstract>() && function.hasAnnotation<Concrete>() ->
                error(EXCEPTION_MULTIPLE_FUNCTION_TYPE_ANNOTATIONS.format(function.name))

            function.hasAnnotation<Abstract>() -> abstractFunctionElement(function)
            function.hasAnnotation<Concrete>() -> concreteFunctionElement(function)
            else -> error(EXCEPTION_FUNCTION_TYPE_ANNOTATION_NOT_FOUND.format(function.name))
        }

    private fun abstractFunctionElement(function: KFunction<*>): Pair<QualifiedType, KFunction<*>> {
        val targetParameter = function.valueParameters
        require(targetParameter.size == 1) { EXCEPTION_INVALID_PARAMETER_COUNT }

        val paramType =
            requireNotNull(targetParameter.firstOrNull()?.type) {
                EXCEPTION_NO_MATCHING_PARAMETER.format(function.name)
            }
        val constructor =
            requireNotNull(
                paramType.jvmErasure.constructors.firstOrNull { constructor ->
                    constructor.hasAnnotation<Supply>()
                },
            ) {
                EXCEPTION_NO_PRIMARY_CONSTRUCTOR_FOUND.format(paramType.jvmErasure)
            }

        return QualifiedType(
            returnType = function.returnType,
            qualifier = findAnnotationOf<Qualifier>(paramType.jvmErasure.annotations)?.annotationClass?.simpleName,
        ) to constructor
    }

    private fun concreteFunctionElement(function: KFunction<*>): Pair<QualifiedType, KFunction<*>> {
        val qualifier = findAnnotationOf<Qualifier>(function.annotations)
        return QualifiedType(
            returnType = function.returnType,
            qualifier = qualifier?.annotationClass?.simpleName,
        ) to function
    }

    private inline fun <reified T : Annotation> findAnnotationOf(annotations: List<Annotation>): Annotation? {
        return annotations.firstOrNull { annotation ->
            annotation.annotationClass.hasAnnotation<T>()
        }
    }

    companion object {
        private const val EXCEPTION_MULTIPLE_FUNCTION_TYPE_ANNOTATIONS =
            "@Supply and @Abstract were used together on %s"
        private const val EXCEPTION_FUNCTION_TYPE_ANNOTATION_NOT_FOUND =
            "@Abstract or @Concrete annotation is required for %s"
        private const val EXCEPTION_INVALID_PARAMETER_COUNT =
            "@Abstract must have only 1 parameter whose type is not abstract."
        private const val EXCEPTION_NO_MATCHING_PARAMETER =
            "No matching parameter found for the function: %s"
        private const val EXCEPTION_NO_PRIMARY_CONSTRUCTOR_FOUND =
            "No primary constructor found for %s"
    }
}
