package com.example.sh1mj1

import androidx.lifecycle.ViewModel
import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.annotation.ViewModelScope
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.component.viewmodelscope.ViewModelScopedInstanceWithKeys
import com.example.sh1mj1.container.AppContainer
import com.example.sh1mj1.container.viewmodelscope.ViewModelComponentContainer
import com.example.sh1mj1.extension.injectableProperties
import com.example.sh1mj1.extension.typeToKClass
import com.example.sh1mj1.extension.withQualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class ViewModelDependencyInjector(
    private val appContainer: AppContainer,
) {
    // TODO: nunu createInstance가 많은 역할을 하고 있는 것 같아요! 메서드를 분리해봐도 좋을 것 같습니다.
    fun <VM : ViewModel> viewModelScopedInstanceWithKeys(modelClass: Class<VM>): ViewModelScopedInstanceWithKeys<VM> {
        val kClass = modelClass.kotlin

        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("ViewModel must have a primary constructor: ${kClass.simpleName}")
        val injectedArgs = constructor.injectableProperties()

        val viewModel = calledConstructor(injectedArgs, constructor)
        val injectedFields: List<KProperty1<VM, *>> = injectableProperties(kClass)

        setField(injectedFields, viewModel)

        val viewModelScopeComponents =
            viewModelScopeComponentKeysWithParameters(injectedArgs) +
                    viewModelScopeComponentKeysWithProperties(injectedFields)
        return ViewModelScopedInstanceWithKeys(
            viewModel = viewModel,
            viewModelScopeComponentsKeys = viewModelScopeComponents,
        )
    }

    private fun viewModelScopeComponentKeysWithParameters(kParameters: List<KParameter>): List<ComponentKey> =
        kParameters.map { kParameter ->
            ComponentKey.of(
                clazz = kParameter.typeToKClass(),
                qualifier = kParameter.withQualifier(),
            )
        }


    private fun <VM : Any> viewModelScopeComponentKeysWithProperties(kProperties: List<KProperty1<VM, *>>): List<ComponentKey> =
        kProperties.filter { it.hasAnnotation<ViewModelScope>() }
            .map { kProperty ->
                ComponentKey.of(
                    clazz = kProperty.typeToKClass(),
                    qualifier = kProperty.withQualifier(),
                )
            }


    private fun <VM : Any> calledConstructor(
        injectedArgs: List<KParameter>,
        constructor: KFunction<VM>,
    ): VM {
        val constructorArgs =
            injectedArgs.map { kParameter ->
                foundDependency(ComponentKey.fromParameter(kParameter))
            }.toTypedArray()

        return constructor.call(*constructorArgs)
    }

    private fun foundDependency(componentKey: ComponentKey): Any = appContainer.find(componentKey)

    private fun <VM : ViewModel> setField(
        injectedFields: List<KProperty1<VM, *>>,
        viewModel: VM,
    ) {
        injectedFields.forEach { field ->
            field.isAccessible = true

            val dependency = foundDependency(ComponentKey.fromProperty(field))
            val kMutableProperty = field as? KMutableProperty<*>
                ?: throw IllegalArgumentException("Field must be mutable but not: ${field.name}")

            kMutableProperty.setter.call(viewModel, dependency)
        }
    }

    fun removeViewModelScopeComponent(
        kClass: KClass<*>,
        qualifier: Qualifier?,
    ) {
        ViewModelComponentContainer.instance().remove(kClass, qualifier)
    }
}

private const val TAG = "ViewModelDependencyInjector"

