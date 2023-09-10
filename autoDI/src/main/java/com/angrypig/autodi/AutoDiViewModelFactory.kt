package com.angrypig.autodi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.angrypig.autodi.autoDIContainer.AutoDIModuleContainer
import kotlin.reflect.full.starProjectedType

@Suppress("UNCHECKED_CAST")
object AutoDiViewModelFactory : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>, extras: CreationExtras): VM {
        return AutoDIModuleContainer.searchViewModelBundle(modelClass.kotlin.starProjectedType)
            .getInstance() as VM
    }
}
