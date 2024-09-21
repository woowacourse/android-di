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
                val componentKey =
                    ComponentKey(
                        clazz = kParameter.type.classifier as KClass<*>,
                        qualifier = kParameter.withQualifier(),
                    )
                foundDependencyWithKey(componentKey)
            }.toTypedArray()

        val viewModel = constructor.call(*constructorArgs)
        return viewModel
    }


    private fun foundDependencyWithKey(componentKey: ComponentKey): Any = appContainer.findWithKey(componentKey)

    private fun <T : Any> setField(
        injectedFields: List<KProperty1<T, *>>,
        viewModel: T,
    ) {
        injectedFields.forEach { field ->
            field.isAccessible = true

            val componentKey =
                ComponentKey(
                    clazz = field.returnType.classifier as KClass<*>,
                    qualifier = field.withQualifier(),
                )

            val dependency = foundDependencyWithKey(componentKey)
            (field as KMutableProperty<*>).setter.call(viewModel, dependency)
        }
    }
}
fun KParameter.withQualifier(): Qualifier? = annotations.filterIsInstance<Qualifier>().firstOrNull()

fun <T : Any> KProperty1<T, *>.withQualifier(): Qualifier? = annotations.filterIsInstance<Qualifier>().firstOrNull()
