package com.angrypig.autodi.autoDIContainer

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
        _value.map { existingModule ->
            if (existingModule.qualifier == autoDIModule.qualifier) {
                existingState = true
                autoDIModule
            } else {
                existingModule
            }
        }
        if (!existingState) _value.add(autoDIModule)
    }

    private fun selectModuleByQualifier(
        existingModule: AutoDIModule,
        autoDIModule: AutoDIModule,
    ) = when (existingModule.qualifier == autoDIModule.qualifier) {
        true -> autoDIModule
        false -> existingModule
    }

    internal fun searchModule(qualifier: String): AutoDIModule? =
        value.find { autoDIModule -> autoDIModule.qualifier == qualifier }

    internal fun <T : Any> searchLifeCycleType(kType: KType, qualifier: String?): T? {
        value.forEach { autoDIModule ->
            val eachSearchResult: T? = autoDIModule.searchLifeCycleType(kType, qualifier)
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
}
