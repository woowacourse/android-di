package com.kmlibs.supplin

import com.kmlibs.supplin.annotations.Abstract
import com.kmlibs.supplin.annotations.Concrete
import com.kmlibs.supplin.model.QualifiedType
import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class FunctionContainer<T : Any>(module: KClass<T>) {
    val qualifiedFunctions: Map<QualifiedType, KFunction<*>> =
        module.declaredMemberFunctions
            .associate { memberFunction ->
                functionByQualifiedType(memberFunction)
            }

    private fun functionByQualifiedType(function: KFunction<*>) =
        when {
            function.hasAnnotation<Abstract>() && function.hasAnnotation<Concrete>() ->
                error("@Supply and @Abstract cannot be used together")

            function.hasAnnotation<Abstract>() ->
                abstractFunctionElement(function)

            function.hasAnnotation<Concrete>() ->
                concreteFunctionElement(function)

            else -> error("@Abstract or @Concrete annotation is required")
        }

    private fun abstractFunctionElement(function: KFunction<*>): Pair<QualifiedType, KFunction<*>> {
        // why last? - the first parameter is the object that function is located
        val paramType = function.parameters.last().type
        val constructor =
            paramType.jvmErasure.primaryConstructor
                ?: throw IllegalStateException("No primary constructor found for ${paramType.jvmErasure}")

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
}

