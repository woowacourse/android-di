package com.angrypig.autodi.autoDIModule

import androidx.lifecycle.ViewModel
import com.angrypig.autodi.AutoDI
import com.angrypig.autodi.ViewModelBundle
import kotlin.reflect.KType

class AutoDIModule(val qualifier: String? = null) {
    private val lifeCycleTypeContainer: LifeCycleTypeContainer = LifeCycleTypeContainer()
    private val androidComponentsContainer: AndroidComponentsContainer =
        AndroidComponentsContainer()

    inline fun <reified T : Any> inject(qualifier: String? = null): T =
        AutoDI.inject()

    fun <T : Any> singleton(qualifier: String? = null, registerBlock: () -> T) {
        lifeCycleTypeContainer.registerSingleton(qualifier, registerBlock)
    }

    fun <T : Any> disposable(qualifier: String? = null, registerBlock: () -> T) {
        lifeCycleTypeContainer.registerDisposable(qualifier, registerBlock)
    }

    fun <VM : ViewModel> viewModel(registerBlock: () -> VM) {
        androidComponentsContainer.registerViewModel(registerBlock)
    }

    internal fun <T : Any> searchLifeCycleType(kType: KType, qualifier: String?): T? =
        lifeCycleTypeContainer.search(kType, qualifier)

    internal fun searchViewModelBundle(kType: KType): ViewModelBundle<*>? =
        androidComponentsContainer.searchViewModelBundle(kType)
}
