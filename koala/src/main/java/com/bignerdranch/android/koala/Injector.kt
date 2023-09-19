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
    private val module: DiModule,
) {

    fun <T : Any> inject(clazz: KClass<T>): T {
        val constructor = clazz.primaryConstructor ?: throw IllegalStateException("찾을 수 없습니다.")
        val args = constructor.parameters.map {
            getParameterInstance(it)
        }
        val instance = constructor.call(*args.toTypedArray())

        val properties = clazz.declaredMemberProperties.filterIsInstance<KMutableProperty1<T, *>>()
            .filter { it.hasAnnotation<KoalaFieldInject>() }
        properties.forEach { property ->
            property.setter.call(instance, getPropertyInstance(property))
        }

        return instance
    }

    private fun getParameterInstance(parameter: KParameter): Any {
        val singletonInstance = Container.instances[parameter::class]
        if (singletonInstance != null) {
            return singletonInstance
        }

        val qualifier =
            parameter.annotations.find { it.annotationClass.hasAnnotation<KoalaQualifier>() }
        if (qualifier != null) {
            return getInstanceWithQualifier(qualifier)
        }
        return getInstanceWithType(parameter.type)
    }

    fun getPropertyInstance(property: KProperty1<*, *>): Any {
        val singletonInstance = Container.instances[property::class]
        if (singletonInstance != null) {
            return singletonInstance
        }
        val qualifier =
            property.annotations.find { it.annotationClass.hasAnnotation<KoalaQualifier>() }
        if (qualifier != null) {
            return getInstanceWithQualifier(qualifier)
        }
        return getInstanceWithType(property.returnType)
    }

    private fun getInstanceWithQualifier(qualifier: Annotation): Any {
        val function = module::class.declaredMemberFunctions.find { func ->
            func.annotations.any { it == qualifier }
        } ?: throw IllegalStateException("찾을 수 없습니다.")

        return callFunction(function)
    }

    private fun getInstanceWithType(type: KType): Any {
        val functions = module::class.declaredMemberFunctions.filter { func ->
            func.returnType == type
        }

        when (functions.size) {
            1 -> return callFunction(functions.first())
            0 -> throw IllegalStateException("찾을 수 없습니다.")
            else -> throw IllegalStateException("여러개 일치할 수 없습니다")
        }
    }

    fun callFunction(function: KFunction<*>): Any {
        val args = arrayListOf<Any>()
        function.valueParameters.forEach { parameter ->
            args.add(getParameterInstance(parameter))
        }
        return function.call(module, *args.toTypedArray())
            ?: throw IllegalStateException("instance를 생성할 수 없습니다.")
    }
}
