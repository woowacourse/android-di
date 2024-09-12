package com.example.sh1mj1

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class DependencyInjector(
    private val appContainer: AppContainer,
) {
    fun <T : Any> createInstance(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin

        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("ViewModel must have a primary constructor: ${kClass.simpleName}")

        val injectedArgs =
            constructor.parameters.filter { kParameter ->
                kParameter.hasAnnotation<Inject>()
            }

        val instance = calledConstructor(injectedArgs, constructor)
        val injectedFields = kClass.memberProperties.filter { it.hasAnnotation<Inject>() }
        setField(injectedFields, instance)

        return instance
    }

    private fun <T : Any> calledConstructor(
        injectedArgs: List<KParameter>,
        constructor: KFunction<T>,
    ): T {
        val constructorArgs =
            injectedArgs.map { kParameter ->
                val qualifier = kParameter.annotations.filterIsInstance<Qualifier>().firstOrNull()
                foundDependency(qualifier, kParameter)
            }.toTypedArray()

        val viewModel = constructor.call(*constructorArgs)
        return viewModel
    }

    private fun <T : Any> setField(
        injectedFields: List<KProperty1<T, *>>,
        viewModel: T,
    ) {
        injectedFields.forEach { field ->
            field.isAccessible = true

            val qualifier = field.annotations.filterIsInstance<Qualifier>().firstOrNull()
            val dependency = foundDependency(qualifier, field)
            (field as KMutableProperty<*>).setter.call(viewModel, dependency)
        }
    }

    private fun <T : Any> foundDependency(
        qualifier: Qualifier?,
        field: KProperty1<T, *>,
    ): Any =
        if (qualifier != null) {
            appContainer.find(field.returnType.classifier as KClass<*>, qualifier)
        } else {
            appContainer.find(field.returnType.classifier as KClass<*>)
        } ?: throw IllegalArgumentException("Unresolved dependency for field: ${field.name}")

    private fun foundDependency(
        qualifier: Qualifier?,
        kParameter: KParameter,
    ): Any =
        if (qualifier != null) {
            appContainer.find(kParameter.type.classifier as KClass<*>, qualifier).also {
                println("Tag $it")
            }
        } else {
            appContainer.find(kParameter.type.classifier as KClass<*>)
        } ?: throw IllegalArgumentException("Unresolved dependency for parameter: ${kParameter.name}")
}
