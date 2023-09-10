package com.angrypig.autodi.autoDIModule.register

import androidx.lifecycle.ViewModel
import com.angrypig.autodi.autoDIModule.ViewModelBundles

class ViewModelRegister(private val viewModels: ViewModelBundles) {
    internal fun <VM : ViewModel> register(initializeMethod: () -> VM) {
        viewModels.add(initializeMethod)
    }
}
