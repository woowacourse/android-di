package com.angrypig.autodi.autoDIModule

import androidx.lifecycle.ViewModel
import com.angrypig.autodi.ViewModelBundle
import com.angrypig.autodi.autoDIModule.register.ViewModelRegister
import kotlin.reflect.KType

internal class AndroidComponentsContainer {
    private val viewModelBundles: ViewModelBundles = ViewModelBundles(mutableListOf())
    private val viewModelRegister: ViewModelRegister = ViewModelRegister(viewModelBundles)

    internal fun <VM : ViewModel> registerViewModel(registerBlock: () -> VM) {
        viewModelRegister.register(registerBlock)
    }

    internal fun searchViewModelBundle(kType: KType): ViewModelBundle<*>? =
        viewModelBundles.search(kType)
}
