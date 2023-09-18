package com.angrypig.autodi.autoDIContainer

import android.content.Context
import androidx.lifecycle.ViewModel
import com.angrypig.autodi.LifeCycleType
import com.angrypig.autodi.ViewModelBundle
import com.angrypig.autodi.autoDIModule.AutoDIModule
import kotlin.reflect.KType

object AutoDIModuleContainer {

    const val NOT_EXIST_DEPENDENCY_ERROR =
        "search 함수로 검색한 dependency 가 존재하지 않습니다. qualifier 혹은 선언 모듈을 확인하세요"
    const val NOT_EXIST_SINGLE_LIFE_CYCLE_TYPE_OVERRIDE_ERROR =
        "override 하려는 lifeCycleType이 존재하지 않습니다. 입력하신 qualifier 혹은 선언 모듈을 확인하세요"
    const val NOT_EXIST_VIEW_MODEL_BUNDLE_OVERRIDE_ERROR =
        "override 하려는 ViewModel이 존재하지 않습니다. 입력하신 선언 모듈을 확인하세요"
    const val APPLICATION_CONTEXT_NOT_INITIALIZE_ERROR =
        "application context를 초기화 하지않고 사용하셨습니다. application context를 등록해주세요"

    private val autoDIModules: AutoDIModules = AutoDIModules(mutableListOf())

    private lateinit var applicationContext: Context

    internal fun registerApplicationContext(applicationContext: Context) {
        this.applicationContext = applicationContext
    }

    internal fun getApplicationContext(): Context {
        if (::applicationContext.isInitialized) return applicationContext
        throw IllegalStateException(APPLICATION_CONTEXT_NOT_INITIALIZE_ERROR)
    }

    internal fun registerModule(autoDIModule: AutoDIModule) {
        autoDIModules.addModule(autoDIModule)
    }

    internal fun overrideModule(qualifier: String, autoDIModule: AutoDIModule) {
        autoDIModules.overrideModule(qualifier, autoDIModule)
    }

    internal fun <T : Any> overrideSingleLifeCycleType(
        kType: KType,
        qualifier: String,
        initializeMethod: () -> T,
    ) {
        val lifeCycleType: LifeCycleType<T> =
            autoDIModules.searchLifeCycleType<T>(kType, qualifier) ?: throw IllegalStateException(
                NOT_EXIST_SINGLE_LIFE_CYCLE_TYPE_OVERRIDE_ERROR,
            )
        lifeCycleType.override(initializeMethod)
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <VM : ViewModel> overrideSingleViewModelBundle(
        kType: KType,
        initializeMethod: () -> VM,
    ) {
        val viewModelBundle: ViewModelBundle<VM> =
            autoDIModules.searchViewModelBundle(kType) as ViewModelBundle<VM>?
                ?: throw IllegalStateException(NOT_EXIST_VIEW_MODEL_BUNDLE_OVERRIDE_ERROR)
        viewModelBundle.override(initializeMethod)
    }

    internal fun <T : Any> searchLifeCycleType(kType: KType, qualifier: String?): LifeCycleType<T> =
        autoDIModules.searchLifeCycleType(kType, qualifier) ?: throw IllegalStateException(
            NOT_EXIST_DEPENDENCY_ERROR,
        )

    internal fun searchViewModelBundle(kType: KType): ViewModelBundle<*> =
        autoDIModules.searchViewModelBundle(kType) ?: throw IllegalStateException(
            NOT_EXIST_DEPENDENCY_ERROR,
        )

    internal fun clear() {
        autoDIModules.clear()
    }
}
