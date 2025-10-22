package com.m6z1.moongdi

import androidx.lifecycle.ViewModel
import java.util.UUID

abstract class BaseViewModel : ViewModel() {
    private val viewModelId: String = UUID.randomUUID().toString()

    init {
        val context = ScopedDependencyInjector.getContext()

        ScopedDependencyInjector.setContext(
            ScopedDependencyInjector.InjectionContext(
                activityId = context.activityId,
                viewModelId = viewModelId,
            ),
        )
    }

    override fun onCleared() {
        super.onCleared()

        ScopedDependencyContainer.clearViewModelScope(viewModelId)
        ScopedDependencyInjector.clearContext()
    }

    companion object {
        internal fun getViewModelId(): String = UUID.randomUUID().toString()
    }
}
