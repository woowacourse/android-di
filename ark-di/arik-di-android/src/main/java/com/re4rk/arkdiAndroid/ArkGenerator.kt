package com.re4rk.arkdiAndroid

import android.content.Context
import com.re4rk.arkdi.ArkContainer

interface ArkGenerator {
    fun createApplicationModule(applicationContext: Context): ArkContainer
    fun createRetainedActivityModule(parentDiModule: ArkContainer, context: Context): ArkContainer
    fun createActivityModule(parentDiModule: ArkContainer, context: Context): ArkContainer
    fun createViewModelModule(parentDiModule: ArkContainer): ArkContainer
}
