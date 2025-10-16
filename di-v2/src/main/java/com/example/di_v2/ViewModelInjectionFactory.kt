package com.example.di_v2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelInjectionFactory(
    private val container: DIContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // KClass로 변환
        val kClass = modelClass.kotlin

        // resolve()로 생성자 주입
        val viewModelInstance = container.resolve(kClass)

        // inject()로 필드 주입
        container.inject(viewModelInstance)

        return viewModelInstance
    }
}
