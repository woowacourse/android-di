package com.re4rk.arkdiAndroid.arkModules

import android.content.Context
import com.re4rk.arkdi.ArkModule

interface ArkModules {
    fun createApplicationModule(applicationContext: Context): ArkModule
    fun createRetainedActivityModule(parentModule: ArkModule, context: Context): ArkModule
    fun createActivityModule(parentModule: ArkModule, context: Context): ArkModule
    fun createViewModelModule(parentModule: ArkModule): ArkModule
}
