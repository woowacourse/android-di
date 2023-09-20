package com.re4rk.arkdiAndroid.arkModules

import android.content.Context
import com.re4rk.arkdi.ArkModule

interface ArkModules {
    val applicationModule: (Context) -> ArkModule
    val retainedActivityModule: (ArkModule, Context) -> ArkModule
    val activityModule: (ArkModule, Context) -> ArkModule
    val viewModelModule: (ArkModule) -> ArkModule
    val serviceModule: (ArkModule) -> ArkModule
}
