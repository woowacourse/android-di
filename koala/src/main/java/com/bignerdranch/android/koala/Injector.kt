package com.bignerdranch.android.koala

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

class Injector(
    private val container: Container,
) {

    fun <T : Any> inject(clazz: KClass<T>): T {
        val constructor = clazz.primaryConstructor ?: throw IllegalStateException("찾을 수 없습니다.")
        val args = constructor.parameters.map {
            getParameterInstance(it)
        }
        val viewModel = constructor.call(*args.toTypedArray())

        val properties = clazz.declaredMemberProperties.filterIsInstance<KMutableProperty1<T, *>>()
        properties.forEach { property ->
            property.setter.call(viewModel, getPropertyInstance(property))
        }

        return viewModel
    }

    private fun getParameterInstance(parameter: KParameter): Any {
        val qualifier =
            parameter.annotations.find { it.annotationClass.hasAnnotation<KoalaQualifier>() }
        if (qualifier != null) {
            return getInstanceWithQualifier(qualifier)
        }
        return getInstanceWithType(parameter.type)
    }

    private fun getPropertyInstance(property: KProperty1<*, *>): Any {
        val qualifier =
            property.annotations.find { it.annotationClass.hasAnnotation<KoalaQualifier>() }
        if (qualifier != null) {
            return getInstanceWithQualifier(qualifier)
        }
        return getInstanceWithType(property.returnType)
    }

    private fun getInstanceWithQualifier(qualifier: Annotation): Any {
        val function = container.javaClass.kotlin.declaredMemberFunctions.find { func ->
            func.annotations.any { it == qualifier }
        } ?: throw IllegalStateException("찾을 수 없습니다.")

        return callFunction(function)
    }

    private fun getInstanceWithType(type: KType): Any {
        val function = container.javaClass.kotlin.declaredMemberFunctions.find { func ->
            func.returnType == type
        } ?: throw IllegalStateException("${type}을 찾을 수 없습니다.")
        return callFunction(function)
    }

    private fun callFunction(function: KFunction<*>): Any {
        val args = arrayListOf<Any>()
        function.valueParameters.forEach { parameter ->
            args.add(getParameterInstance(parameter))
        }
        return function.call(container, *args.toTypedArray())
            ?: throw IllegalStateException("instance를 생성할 수 없습니다.")
    }
}
