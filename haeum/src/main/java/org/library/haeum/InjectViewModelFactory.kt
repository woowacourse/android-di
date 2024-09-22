package org.library.haeum

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

class InjectViewModelFactory(
    private val viewModelClass: KClass<out ViewModel>,
    private val container: Container? = Container.container,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructors = viewModelClass.primaryConstructor ?: throw IllegalArgumentException("ViewModel에 기본 생성자가 없습니다: ${viewModelClass.qualifiedName}")
        val parameters = constructors.parameters
        val arguments =
            parameters.map {
                container?.getKParameterInstance(it) as KParameter
            }.toTypedArray()
        val viewModel = constructors.call(*arguments)
        container?.injectTo(viewModel)
        return viewModel as T
    }
}

inline fun <reified T : ViewModel> ComponentActivity.createViewModel(): T {
    val viewModel: T by viewModels {
        InjectViewModelFactory(T::class)
    }
    return viewModel
}
