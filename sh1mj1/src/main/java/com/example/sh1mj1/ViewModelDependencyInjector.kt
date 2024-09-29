package com.example.sh1mj1

import androidx.lifecycle.ViewModel
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.component.viewmodelscope.ComponentKeysForViewModel
import com.example.sh1mj1.component.viewmodelscope.viewModelScopeParameterKeys
import com.example.sh1mj1.component.viewmodelscope.viewModelScopePropertyKeys
import com.example.sh1mj1.container.AppContainer
import com.example.sh1mj1.extension.injectableProperties
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class ViewModelDependencyInjector(
    private val appContainer: AppContainer,
) {
    fun <VM : ViewModel> viewModelScopedInstanceWithKeys(modelClass: Class<VM>): ComponentKeysForViewModel<VM> {
        val kClass = modelClass.kotlin

        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("ViewModel must have a primary constructor: ${kClass.simpleName}")
        val injectedArgs = constructor.injectableProperties()

        val viewModel = calledConstructor(injectedArgs, constructor)
        val injectedFields: List<KProperty1<VM, *>> = injectableProperties(kClass)

        setField(injectedFields, viewModel)

        val viewModelScopeComponents =
            injectedArgs.viewModelScopeParameterKeys() + injectedFields.viewModelScopePropertyKeys()
        return ComponentKeysForViewModel(
            viewModel = viewModel,
            componentKeys = viewModelScopeComponents,
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
}

private const val TAG = "ViewModelDependencyInjector"

