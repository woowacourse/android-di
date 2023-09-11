package com.angrypig.autodi

import com.angrypig.autodi.autoDIContainer.AutoDIModuleContainer
import com.angrypig.autodi.autoDIModule.AutoDIModule
import kotlin.reflect.KType
import kotlin.reflect.typeOf

object AutoDI {
    operator fun invoke(init: AutoDI.() -> Unit) {
        this.init()
    }

    fun registerModule(autoDIModule: AutoDIModule) {
        AutoDIModuleContainer.registerModule(autoDIModule)
    }

    fun overrideModule(qualifier: String, autoDIModule: AutoDIModule) {
        AutoDIModuleContainer.overrideModule(qualifier, autoDIModule)
    }

    inline fun <reified T : Any> inject(qualifier: String? = null): T {
        val kType = typeOf<T>()
        return publishedSearchLifeCycleType(kType, qualifier)
    }

    @PublishedApi
    internal fun <T> publishedSearchLifeCycleType(kType: KType, qualifier: String?): T =
        AutoDIModuleContainer.searchLifeCycleType(kType, qualifier)
}
