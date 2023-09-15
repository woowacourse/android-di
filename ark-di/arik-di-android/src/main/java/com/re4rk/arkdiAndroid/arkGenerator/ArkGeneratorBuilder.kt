package com.re4rk.arkdiAndroid.arkGenerator

import android.content.Context
import com.re4rk.arkdi.ArkContainer

class ArkGeneratorBuilder {
    var applicationModule: (Context) -> ArkContainer =
        { ArkContainer() }
    var retainedActivityModule: (ArkContainer, Context) -> ArkContainer =
        { parent, _ -> ArkContainer(parent) }
    var activityModule: (ArkContainer, Context) -> ArkContainer =
        { parent, _ -> ArkContainer(parent) }
    var viewModelModule: (ArkContainer) -> ArkContainer =
        { parent -> ArkContainer(parent) }

    fun build(): ArkGenerator {
        return object : ArkGenerator {
            override fun createApplicationModule(applicationContext: Context): ArkContainer =
                applicationModule(applicationContext)

            override fun createRetainedActivityModule(
                parent: ArkContainer,
                context: Context,
            ): ArkContainer = retainedActivityModule(parent, context)

            override fun createActivityModule(
                parent: ArkContainer,
                context: Context,
            ): ArkContainer = activityModule(parent, context)

            override fun createViewModelModule(parent: ArkContainer): ArkContainer =
                viewModelModule(parent)
        }
    }
}
