package com.on.di_library.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DiViewModelFactory(
    private val activityID: Long,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = DiContainer.getInstance(
            kClass = modelClass.kotlin,
            scopeId = activityID
        )

        if (viewModel is DiViewModel) {
            viewModel.viewModelId = System.currentTimeMillis()
        }

        return viewModel
    }
}