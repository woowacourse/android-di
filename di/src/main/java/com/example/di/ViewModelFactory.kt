package com.example.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        val constructor: KFunction<VM> =
            modelClass.kotlin.primaryConstructor ?: error("${modelClass.name}의 주 생성자를 찾을 수 없습니다.")

        val parameters: Array<Any> =
            constructor.parameters
                .map { parameter: KParameter ->
                    DependencyProvider.dependency(Identifier.of(parameter))
                }.toTypedArray()

        val viewModel: VM = constructor.call(*parameters)
        DependencyProvider.injectFields(viewModel)
        return viewModel
    }
}

inline fun <reified VM : ViewModel> ComponentActivity.injectableViewModel(): Lazy<VM> =
    ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = { ViewModelFactory() },
    )
