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
            override fun createApplicationModule(applicationContext: Context): ArkModule =
                applicationModule(applicationContext)

            override fun createRetainedActivityModule(
                parentModule: ArkModule,
                context: Context,
            ): ArkModule = retainedActivityModule(parentModule, context)

            override fun createActivityModule(
                parentModule: ArkModule,
                context: Context,
            ): ArkModule = activityModule(parentModule, context)

            override fun createViewModelModule(parentModule: ArkModule): ArkModule =
                viewModelModule(parentModule)

            override fun createServiceModule(parentModule: ArkModule): ArkModule =
                serviceModule(parentModule)
        }
    }
}
