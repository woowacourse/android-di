package io.hyemdooly.androiddi.base

import androidx.lifecycle.ViewModel
import io.hyemdooly.di.Module

open class HyemdoolyViewModel : ViewModel() {
    lateinit var activityRetainedModule: Module
    lateinit var activityModule: Module
    lateinit var viewModelModule: Module

    val isInitailzeModule: Boolean
        get() = ::activityRetainedModule.isInitialized && ::activityModule.isInitialized && ::viewModelModule.isInitialized
}
