package com.on.di_library.di

import androidx.lifecycle.ViewModel

abstract class DiViewModel : ViewModel() {
    var viewModelId: Long = 0L
        internal set

    init {
        DiContainer.injectFieldProperties(
            implementClass = this::class,
            instance = this,
            scopeId = viewModelId
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (viewModelId != 0L) {
            ScopeContainer.clearViewModelScope(viewModelId)
        }
    }
}