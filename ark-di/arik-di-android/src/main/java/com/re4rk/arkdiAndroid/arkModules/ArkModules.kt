package com.re4rk.arkdiAndroid.arkModules

import android.content.Context
import com.re4rk.arkdi.ArkContainer

interface ArkModules {
    fun createApplicationModule(applicationContext: Context): ArkContainer
    fun createRetainedActivityModule(parent: ArkContainer, context: Context): ArkContainer
    fun createActivityModule(parent: ArkContainer, context: Context): ArkContainer
    fun createViewModelModule(parent: ArkContainer): ArkContainer
}
