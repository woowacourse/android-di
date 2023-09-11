package com.angrypig.autodi.autoDIContainer

import com.angrypig.autodi.LifeCycleType
import com.angrypig.autodi.ViewModelBundle
import com.angrypig.autodi.autoDIModule.AutoDIModule
import kotlin.reflect.KType

object AutoDIModuleContainer {

    const val NOT_EXIST_DEPENDENCY_ERROR =
        "search 함수로 검색한 dependency 가 존재하지 않습니다. qualifier 혹은 선언 모듈을 확인하세요"
    const val NOT_EXIST_SINGLE_LIFE_CYCLE_TYPE_OVERRIDE_ERROR =
        "override 하려는 lifeCycleType이 존재하지 않습니다. 입력하신 qualifier 혹은 선언 모듈을 확인하세요"

    private val autoDIModules: AutoDIModules = AutoDIModules(mutableListOf())

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
