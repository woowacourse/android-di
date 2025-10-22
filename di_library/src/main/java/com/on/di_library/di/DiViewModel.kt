package com.on.di_library.di

import androidx.lifecycle.ViewModel

abstract class DiViewModel : ViewModel() {
    val viewModelID: Long = System.currentTimeMillis()

    override fun onCleared() {
        super.onCleared()
        ScopeContainer.clearViewModelScope(viewModelID)
    }
}