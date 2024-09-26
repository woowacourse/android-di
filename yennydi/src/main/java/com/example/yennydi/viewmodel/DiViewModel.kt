package com.example.yennydi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.yennydi.di.DependencyContainer

interface Injectable {
    fun inject(container: DependencyContainer)
}

abstract class DiViewModel : ViewModel(), Injectable {
    lateinit var instanceContainer: DependencyContainer

    override fun inject(container: DependencyContainer) {
        instanceContainer = container
    }

    override fun onCleared() {
        super.onCleared()
        instanceContainer.clear()
    }
}
