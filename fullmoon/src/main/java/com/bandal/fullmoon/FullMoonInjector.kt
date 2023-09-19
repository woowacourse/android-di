package com.bandal.fullmoon

import com.bandal.fullmoon.DIError.NotFoundPrimaryConstructor
import com.bandal.fullmoon.DIError.NotFoundQualifierOrInstance
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class FullMoonInjector(private val appContainer: AppContainer) {

    fun <T : Any> inject(kClass: KClass<T>): T {
        return (getInstance(kClass) ?: createInstance(kClass)) as T
    }

    private fun getInstance(kClass: KClass<*>): Any? {
        return appContainer.getSavedInstance(InjectType.from(kClass), kClass)
    }

    private fun getInstance(kProperty: KProperty1<*, *>): Any? {
        return appContainer.getSavedInstance(InjectType.from(kProperty), kProperty)
    }

    private fun getInstance(kParameter: KParameter): Any? {
        return appContainer.getSavedInstance(InjectType.from(kParameter), kParameter)
    }

    private fun <T : Any> createInstance(kClass: KClass<T>): T {
        val constructor = kClass.primaryConstructor ?: throw NotFoundPrimaryConstructor()
        val parameters = constructor.parameters.map { kParameter ->
            getInstance(kParameter) ?: createParameterInstance(kParameter)
        }
        val instance = constructor.call(*parameters.toTypedArray())
        return instance.apply { injectFields(this) }
    }

    private fun <T : Any> injectFields(instance: T) {
        instance::class.declaredMemberProperties
            .filter {
                it.hasAnnotation<Qualifier>() || it.hasAnnotation<FullMoonInject>()
            }
            .forEach { property ->
                property.isAccessible = true
                (property as KMutableProperty<*>).setter.call(
                    instance,
                    getInstance(property) ?: createPropertyInstance(property),
                )
            }
    }

    private fun createParameterInstance(parameter: KParameter): Any {
        if (parameter.hasAnnotation<Qualifier>()) {
            return createInstanceWithQualifier(parameter.getQualifierKey())
        }
        return createInstanceWithType(parameter.type)
    }

    private fun createPropertyInstance(property: KProperty1<*, *>): Any {
        val qualifier: Annotation =
            property.findAnnotation<Qualifier>()
                ?: return createInstanceWithType(property.returnType)

        return createInstanceWithQualifier((qualifier as Qualifier).key)
    }

    private fun createInstanceWithQualifier(qualifierKey: String): Any {
        val function = appContainer::class.declaredMemberFunctions.find { func ->
            func.annotations.contains(Qualifier(qualifierKey))
        } ?: throw DIError.NotFoundCreateFunction()

        return callCreateFunction(function)
    }

    private fun createInstanceWithType(type: KType): Any {
        val functions: List<KFunction<*>> =
            appContainer::class.declaredMemberFunctions.filter { func ->
                !func.hasAnnotation<Qualifier>() && (func.returnType == type)
            }

        when (functions.size) {
            1 -> return callCreateFunction(functions.first())
            0 -> throw DIError.NotFoundCreateFunction()
            else -> throw DIError.NotAllowedDuplicatedCreateFunction()
        }
    }

    private fun callCreateFunction(function: KFunction<*>): Any {
        val parameterInstances = function.valueParameters.map { parameter ->
            createParameterInstance(parameter)
        }

        val instance: Any = function.call(appContainer, *parameterInstances.toTypedArray())
            ?: throw DIError.NotFoundCreateFunction()

        if (function.hasAnnotation<SingleTone>()) {
            val kClass: KClass<*> = function.returnType.jvmErasure
            appContainer.addInstance(
                injectType = InjectType.from(function),
                type = kClass,
                instance = instance,
                qualifierKey = function.findAnnotation<Qualifier>()?.key,
            )
        }

        return instance
    }

    private fun KParameter.getQualifierKey(): String {
        return this.findAnnotation<Qualifier>()?.key
            ?: throw NotFoundQualifierOrInstance(this.type.jvmErasure)
    }
}
