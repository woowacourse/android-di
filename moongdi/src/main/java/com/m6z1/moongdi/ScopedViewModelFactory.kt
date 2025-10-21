package com.m6z1.moongdi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.UUID

class ScopedViewModelFactory(
    private val activityId: String,
    private val viewModelCreator: () -> ViewModel,
) : ViewModelProvider.Factory {
    private val viewModelId = UUID.randomUUID().toString()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        ScopedDependencyInjector.setContext(
            ScopedDependencyInjector.InjectionContext(
                activityId = activityId,
                viewModelId = viewModelId,
            ),
        )

        return viewModelCreator() as T
    }
}

inline fun <reified VM : ViewModel> BaseActivity.scopedViewModel(): Lazy<VM> =
    lazy {
        val factory =
            ScopedViewModelFactory(activityId = activityId) {
                ScopedDependencyInjector.inject<VM>()
            }
        ViewModelProvider(this, factory)[VM::class.java]
    }
