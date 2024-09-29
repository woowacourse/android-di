package com.example.sh1mj1.component.viewmodelscope

import androidx.lifecycle.ViewModel
import com.example.sh1mj1.annotation.Inject
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.container.viewmodelscope.ViewModelComponentContainer
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation

/**
 * @param viewModel Instance of ViewModel
 * @param viewModelScopeComponentsKeys List of ComponentKey of ViewModel's scope
 */
data class ViewModelScopedInstanceWithKeys<VM : ViewModel>(
    val viewModel: VM,
    val viewModelScopeComponentsKeys: List<ComponentKey>,
) {
    init {
        println("$TAG Created viewModel: $viewModel keys $viewModelScopeComponentsKeys")

        viewModel.addCloseable {
            println("$TAG Closed viewModel: $viewModel keys $viewModelScopeComponentsKeys")
            viewModelScopeComponentsKeys.forEach { componentKey ->
                ViewModelComponentContainer.instance().remove(
                    componentKey.clazz,
                    componentKey.qualifier
                ).also {
                    println("$TAG Removed ${componentKey.javaClass.simpleName} viewModel: $viewModel key $componentKey")
                }
            }
        }
    }
}

private const val TAG = "ViewModelScopedInstance"

