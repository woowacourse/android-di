package com.example.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelInjectionFactory(
    private val container: DIContainer,
    private val owner: Any,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // KClass로 변환
        val kClass = modelClass.kotlin

        // resolve()로 생성자 주입
        val viewModelInstance = container.get(kClass, owner = owner)

        return viewModelInstance
    }
}
