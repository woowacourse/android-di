package com.android.diandroid

import androidx.lifecycle.ViewModel
import com.android.di.component.DiContainer
import com.android.di.component.DiInjector

// TODO : fix
abstract class ViewModelInjector(
    private val applicationInjector: ApplicationInjector,
) : ViewModel() {
    private val diInjector: DiInjector = createViewModelInjector()

    init {
        applicationInjector.saveInjector(extractKey(), diInjector)
    }

    override fun onCleared() {
        super.onCleared()
        applicationInjector.removeInjector(extractKey())
    }

    private fun extractKey(): String = this::class.java.name

    private fun createViewModelInjector(): DiInjector {
        return DiInjector(loadViewModelContainer())
    }

    private fun loadViewModelContainer(): DiContainer {
        return DiContainer(parentContainer = applicationInjector.getApplicationContainer())
    }
}
