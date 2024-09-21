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
        /*
        주입 대상이나 Qualifier가 많아질 경우 성능에 영향을 미칠 수 있을 것 같아요.
        개선할 수 있는 방법이 있을까요?
         */
        val constructorArgs =
            injectedArgs.map { kParameter ->
//                val qualifier = kParameter.withQualifier()
//                val kClass = kParameter.type.classifier as KClass<*>
                val componentKey =
                    ComponentKey(
                        clazz = kParameter.type.classifier as KClass<*>,
                        qualifier = kParameter.withQualifier(),
                    )
                foundDependencyWithKey(componentKey)
//                foundDependency(qualifier, kClass)
            }.toTypedArray()

        val viewModel = constructor.call(*constructorArgs)
        return viewModel
    }

    private fun KParameter.withQualifier(): Qualifier? = annotations.filterIsInstance<Qualifier>().firstOrNull()

    private fun foundDependencyWithKey(componentKey: ComponentKey): Any = appContainer.findWithKey(componentKey)

    private fun <T : Any> setField(
        injectedFields: List<KProperty1<T, *>>,
        viewModel: T,
    ) {
        injectedFields.forEach { field ->
            field.isAccessible = true

//            val qualifier = field.withQualifier()
//            val kClass = field.returnType.classifier as KClass<*>
            val componentKey =
                ComponentKey(
                    clazz = field.returnType.classifier as KClass<*>,
                    qualifier = field.withQualifier(),
                )

//            val dependency = foundDependency(qualifier, kClass)
            val dependency = foundDependencyWithKey(componentKey)
            (field as KMutableProperty<*>).setter.call(viewModel, dependency)
        }
    }

    private fun <T : Any> KProperty1<T, *>.withQualifier(): Qualifier? = annotations.filterIsInstance<Qualifier>().firstOrNull()
}
