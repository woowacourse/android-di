package com.medandro.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.full.primaryConstructor

object ViewModelFactory {
    fun create(diContainer: DIContainer): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // ViewModel을 직접 생성
                val viewModel: T = createViewModelInstance(modelClass.kotlin) as T

                // 생성된 ViewModel에 필드 주입
                diContainer.injectFields(viewModel)
                return viewModel
            }
        }
    }

    private fun <T : ViewModel> createViewModelInstance(viewModelClass: kotlin.reflect.KClass<T>): T {
        val constructor =
            viewModelClass.primaryConstructor
                ?: throw IllegalStateException(
                    "${viewModelClass.simpleName}는 매개변수가 없는 주 생성자가 없어 ViewModel을 생성할 수 없습니다.",
                )

        // 생성자 파라미터가 없는 경우 (매개변수 없는 생성자)
        if (constructor.parameters.isEmpty()) {
            return constructor.call()
        }

        // 생성자에 파라미터가 있는 경우 (의존성 주입 필요)
        throw IllegalStateException(
            "${viewModelClass.simpleName}의 생성자에 매개변수가 있습니다. " +
                "ViewModel은 매개변수 없는 생성자를 사용하고 필드 주입으로 의존성을 해결하세요.",
        )
    }
}
