package com.example.sh1mj1.component.viewmodelscope

import androidx.lifecycle.ViewModel
import com.example.sh1mj1.annotation.ViewModelScope
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.container.viewmodelscope.ViewModelScopeComponentContainer
import com.example.sh1mj1.extension.typeToKClass
import com.example.sh1mj1.extension.withQualifier
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation

/**
 * @param viewModel Instance of ViewModel
 * @param componentKeys List of ComponentKey of ViewModel's scope
 */
data class ComponentKeysForViewModel<VM : ViewModel>(
    val viewModel: VM,
    val componentKeys: List<ComponentKey>,
) {
    private val viewModelScopeComponents = ViewModelScopeComponentContainer.instance()

    init {
        viewModel.addCloseable {
            componentKeys.forEach { componentKey ->
                viewModelScopeComponents.remove(
                    componentKey.clazz,
                    componentKey.qualifier,
                )
            }
        }
    }
}

fun List<KParameter>.viewModelScopeParameterKeys(): List<ComponentKey> =
    this.map { kParameter ->
        ComponentKey.of(
            clazz = kParameter.typeToKClass(),
            qualifier = kParameter.withQualifier(),
        )
    }

fun <VM : Any> List<KProperty1<VM, *>>.viewModelScopePropertyKeys(): List<ComponentKey> =
    this.filter { it.hasAnnotation<ViewModelScope>() }
        .map { kProperty ->
            ComponentKey.of(
                clazz = kProperty.typeToKClass(),
                qualifier = kProperty.withQualifier(),
            )
        }
