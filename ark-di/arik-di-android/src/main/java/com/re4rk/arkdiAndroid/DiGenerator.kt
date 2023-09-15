package com.re4rk.arkdiAndroid

import android.content.Context
import com.re4rk.arkdi.DiContainer

interface DiGenerator {
    fun createApplicationModule(applicationContext: Context): DiContainer
    fun createRetainedActivityModule(parentDiModule: DiContainer, context: Context): DiContainer
    fun createActivityModule(parentDiModule: DiContainer, context: Context): DiContainer
    fun createViewModelModule(parentDiModule: DiContainer): DiContainer
}
