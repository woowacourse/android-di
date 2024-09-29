package com.example.sh1mj1.component.viewmodelscope

import androidx.lifecycle.ViewModel
import com.example.sh1mj1.annotation.Inject
import com.example.sh1mj1.component.singleton.ComponentKey
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
)

