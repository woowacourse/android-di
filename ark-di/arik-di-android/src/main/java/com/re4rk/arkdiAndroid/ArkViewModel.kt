package com.re4rk.arkdiAndroid

import androidx.lifecycle.ViewModel
import com.re4rk.arkdi.ArkModule

class ArkViewModel : ViewModel() {
    lateinit var ownerRetainedModule: ArkModule
    lateinit var ownerModule: ArkModule
    lateinit var viewModelModule: ArkModule

    val isInitialized
        get() = ::ownerRetainedModule.isInitialized &&
            ::ownerModule.isInitialized &&
            ::viewModelModule.isInitialized
}
