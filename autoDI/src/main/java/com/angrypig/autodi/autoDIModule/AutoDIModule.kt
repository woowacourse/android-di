package com.angrypig.autodi.autoDIModule

import android.content.Context
import androidx.lifecycle.ViewModel
import com.angrypig.autodi.AutoDI
import com.angrypig.autodi.LifeCycleType
import com.angrypig.autodi.ViewModelBundle
import com.angrypig.autodi.autoDIContainer.AutoDIModuleContainer
import kotlin.reflect.KType

class AutoDIModule(internal var qualifier: String? = null) {
    private val lifeCycleTypeContainer: LifeCycleTypeContainer = LifeCycleTypeContainer()
    private val androidComponentsContainer: AndroidComponentsContainer =
        AndroidComponentsContainer()

    inline fun <reified T : Any> inject(qualifier: String? = null): T =
        AutoDI.inject()

    fun injectApplicationContext(): Context = AutoDIModuleContainer.getApplicationContext()

    fun <T : Any> singleton(qualifier: String? = null, registerBlock: () -> T) {
        lifeCycleTypeContainer.registerSingleton(qualifier, registerBlock)
    }

    fun <T : Any> disposable(qualifier: String? = null, registerBlock: () -> T) {
        lifeCycleTypeContainer.registerDisposable(qualifier, registerBlock)
    }

    fun <VM : ViewModel> viewModel(registerBlock: () -> VM) {
        androidComponentsContainer.registerViewModel(registerBlock)
    }

    internal fun <T : Any> searchLifeCycleType(
        kType: KType,
        qualifier: String?,
    ): LifeCycleType<T>? =
        lifeCycleTypeContainer.search(kType, qualifier)

    internal fun searchViewModelBundle(kType: KType): ViewModelBundle<*>? =
        androidComponentsContainer.searchViewModelBundle(kType)
}
