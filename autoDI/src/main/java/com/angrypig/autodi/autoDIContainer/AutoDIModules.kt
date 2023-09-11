package com.angrypig.autodi.autoDIContainer

import com.angrypig.autodi.LifeCycleType
import com.angrypig.autodi.ViewModelBundle
import com.angrypig.autodi.autoDIModule.AutoDIModule
import kotlin.reflect.KType

class AutoDIModules(value: MutableList<AutoDIModule>) {
    private val _value: MutableList<AutoDIModule> = value
    val value: List<AutoDIModule> get() = _value.toList()

    internal fun addModule(autoDIModule: AutoDIModule) {
        if (autoDIModule.qualifier != null) {
            replaceOrAddByQualifier(autoDIModule)
        } else {
            _value.add(autoDIModule)
        }
    }

    private fun replaceOrAddByQualifier(autoDIModule: AutoDIModule) {
        var existingState = false
        applyChangesToValue(
            value.map { existingModule ->
                if (existingModule.qualifier == autoDIModule.qualifier) {
                    existingState = true
                    autoDIModule
                } else {
                    existingModule
                }
            },
        )
        if (!existingState) _value.add(autoDIModule)
    }

    internal fun overrideModule(qualifier: String, autoDIModule: AutoDIModule) {
        var existingState = false
        applyChangesToValue(
            value.map { existingModule ->
                if (existingModule.qualifier == qualifier) {
                    existingState = true
                    autoDIModule
                } else {
                    existingModule
                }
            },
        )
        if (!existingState) throw IllegalStateException(NOT_EXISTING_MODULE_OVERRIDE_ERROR)
    }

    private fun applyChangesToValue(newValue: List<AutoDIModule>) {
        _value.clear()
        _value.addAll(newValue)
    }

    internal fun <T : Any> searchLifeCycleType(
        kType: KType,
        qualifier: String?,
    ): LifeCycleType<T>? {
        value.forEach { autoDIModule ->
            val eachSearchResult: LifeCycleType<T>? =
                autoDIModule.searchLifeCycleType(kType, qualifier)
            if (eachSearchResult != null) return eachSearchResult
        }
        return null
    }

    internal fun searchViewModelBundle(kType: KType): ViewModelBundle<*>? {
        value.forEach { autoDIModule ->
            val eachSearchResult: ViewModelBundle<*>? = autoDIModule.searchViewModelBundle(kType)
            if (eachSearchResult != null) return eachSearchResult
        }
        return null
    }

    internal fun clear() {
        _value.clear()
    }

    companion object {
        private const val NOT_EXISTING_MODULE_OVERRIDE_ERROR =
            "존재하지 않는 모듈에 대한 override 가 일어났습니다. qualifier를 확인하세요"
    }
}
