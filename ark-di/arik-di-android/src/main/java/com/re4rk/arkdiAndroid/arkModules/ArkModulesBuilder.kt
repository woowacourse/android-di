package com.re4rk.arkdiAndroid.arkModules

import android.content.Context
import com.re4rk.arkdi.ArkModule

class ArkModulesBuilder {
    var applicationModule: (Context) -> ArkModule =
        { ArkModule() }
    var retainedActivityModule: (ArkModule, Context) -> ArkModule =
        { parentModule, _ -> ArkModule(parentModule) }
    var activityModule: (ArkModule, Context) -> ArkModule =
        { parentModule, _ -> ArkModule(parentModule) }
    var viewModelModule: (ArkModule) -> ArkModule =
        { parentModule -> ArkModule(parentModule) }
    var serviceModule: (ArkModule) -> ArkModule =
        { parentModule -> ArkModule(parentModule) }

    fun build(): ArkModules {
        return object : ArkModules {
            override val applicationModule = this@ArkModulesBuilder.applicationModule
            override val retainedActivityModule = this@ArkModulesBuilder.retainedActivityModule
            override val activityModule = this@ArkModulesBuilder.activityModule
            override val viewModelModule = this@ArkModulesBuilder.viewModelModule
            override val serviceModule = this@ArkModulesBuilder.serviceModule
        }
    }
}
