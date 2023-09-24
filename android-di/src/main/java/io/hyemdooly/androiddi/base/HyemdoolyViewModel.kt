package io.hyemdooly.androiddi.base

import androidx.lifecycle.ViewModel
import io.hyemdooly.di.Module

open class HyemdoolyViewModel : ViewModel() {
    lateinit var viewModelModule: Module
}
