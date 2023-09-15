package com.re4rk.arkdiAndroid

import androidx.lifecycle.ViewModel
import com.re4rk.arkdi.ArkContainer

class ArkViewModel : ViewModel() {
    lateinit var ownerRetainedArkContainer: ArkContainer
    lateinit var ownerArkContainer: ArkContainer
    lateinit var viewModelArkContainer: ArkContainer

    val isInitialized
        get() = ::ownerRetainedArkContainer.isInitialized &&
            ::ownerArkContainer.isInitialized &&
            ::viewModelArkContainer.isInitialized
}
