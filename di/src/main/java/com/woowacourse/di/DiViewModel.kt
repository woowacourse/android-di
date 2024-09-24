package com.woowacourse.di

import androidx.lifecycle.ViewModel

abstract class DiViewModel : ViewModel() {
    abstract val module : Module

    override fun onCleared() {
        super.onCleared()
        module.clear()
    }
}
