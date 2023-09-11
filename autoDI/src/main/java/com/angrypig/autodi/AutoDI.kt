package com.angrypig.autodi

import androidx.lifecycle.ViewModel
import com.angrypig.autodi.autoDIContainer.AutoDIModuleContainer
import com.angrypig.autodi.autoDIModule.AutoDIModule
import com.angrypig.autodi.autoDIModule.autoDIModule
import kotlin.reflect.KType
import kotlin.reflect.typeOf

object AutoDI {
    private const val OVERRIDE_KTYPE_NULL_ERROR = "Override 하려는 인스턴스의 타입값이 Null 로 입력되었습니다."

    operator fun invoke(init: AutoDI.() -> Unit) {
        this.init()
    }

    fun registerModule(autoDIModule: AutoDIModule) {
        AutoDIModuleContainer.registerModule(autoDIModule)
    }

    fun overrideModule(qualifier: String, autoDIModule: AutoDIModule) {
        AutoDIModuleContainer.overrideModule(qualifier, autoDIModule)
    }

    inline fun <reified T : Any> overrideSingleLifeCycleType(
        qualifier: String,
        noinline initializeMethod: () -> T,
    ) {
        val kType = typeOf<T>()
        publishedOverrideSingleLifeCycleType(kType, qualifier, initializeMethod)
    }

    @PublishedApi
    internal fun <T : Any> publishedOverrideSingleLifeCycleType(
        kType: KType,
        qualifier: String,
        initializeMethod: () -> T,
    ) {
        AutoDIModuleContainer.overrideSingleLifeCycleType<T>(kType, qualifier, initializeMethod)
    }

    inline fun <reified VM : ViewModel> overrideSingleViewModel(
        noinline initializeMethod: () -> VM,
    ) {
        val kType = typeOf<VM>()
        publishedOverrideViewModelBundle(kType, initializeMethod)
    }

    @PublishedApi
    internal fun <VM : ViewModel> publishedOverrideViewModelBundle(
        kType: KType,
        initializeMethod: () -> VM,
    ) {
        AutoDIModuleContainer.overrideSingleViewModelBundle<VM>(kType, initializeMethod)
    }

    inline fun <reified T : Any> inject(qualifier: String? = null): T {
        val kType = typeOf<T>()
        return publishedSearchLifeCycleType(kType, qualifier)
    }

    @PublishedApi
    internal fun <T : Any> publishedSearchLifeCycleType(kType: KType, qualifier: String?): T =
        AutoDIModuleContainer.searchLifeCycleType<T>(kType, qualifier).getInstance()

    fun clearModuleContainer() {
        AutoDIModuleContainer.clear()
    }
}
