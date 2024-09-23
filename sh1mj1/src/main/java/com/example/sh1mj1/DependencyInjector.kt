package com.example.sh1mj1

import com.example.sh1mj1.annotation.Inject
import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.annotation.ViewModelScope
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.container.AppContainer
import com.example.sh1mj1.container.viewmodelscope.ViewModelComponentContainer
import com.example.sh1mj1.extension.withQualifier
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
    fun <T : Any> createInstance(
        modelClass: Class<T>,
    ): Pair<T, List<Pair<KClass<*>, Qualifier?>>> {
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

        val viewModelScopeConstructorParam =
            injectedArgs.filter { it.hasAnnotation<ViewModelScope>() }.map { it.type.classifier as KClass<*> to it.withQualifier() }
        val viewModelScopeFields =
            injectedFields.filter { it.hasAnnotation<ViewModelScope>() }.map { it.returnType.classifier as KClass<*>  to it.withQualifier()}

        val viewModelScopeComponents = viewModelScopeConstructorParam + viewModelScopeFields

        return instance to viewModelScopeComponents
    }

    private fun <T : Any> calledConstructor(
        injectedArgs: List<KParameter>,
        constructor: KFunction<T>,
    ): T {
        val constructorArgs =
            injectedArgs.map { kParameter ->
                val componentKey =
                    ComponentKey.of(
                        clazz = kParameter.type.classifier as KClass<*>,
                        qualifier = kParameter.withQualifier(),
                    )
                foundDependency(componentKey)
            }.toTypedArray()

        val viewModel = constructor.call(*constructorArgs)
        return viewModel
    }

    private fun foundDependency(componentKey: ComponentKey): Any = appContainer.find(componentKey)

    private fun <T : Any> setField(
        injectedFields: List<KProperty1<T, *>>,
        viewModel: T,
    ) {
        injectedFields.forEach { field ->
            field.isAccessible = true

            val componentKey =
                ComponentKey.of(
                    clazz = field.returnType.classifier as KClass<*>,
                    qualifier = field.withQualifier(),
                )

            val dependency = foundDependency(componentKey)
            val kMutableProperty = field as? KMutableProperty<*>
                ?: throw IllegalArgumentException("Field must be mutable but not: ${field.name}")
            kMutableProperty.setter.call(viewModel, dependency)
        }
    }

    fun removeViewModelScopeComponent(kClass: KClass<*>, qualifier: Qualifier?) {
        ViewModelComponentContainer.instance().remove(kClass, qualifier)
    }
}
