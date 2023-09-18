package com.angrypig.autodi.autoDIModule

import com.angrypig.autodi.LifeCycleType
import com.angrypig.autodi.autoDIModule.LifeCycleTypes.*
import com.angrypig.autodi.autoDIModule.register.DisposableRegister
import com.angrypig.autodi.autoDIModule.register.SingletonRegister
import kotlin.reflect.KType

internal class LifeCycleTypeContainer {
    private val singletons: Singletons = Singletons(mutableListOf())
    private val disposables: Disposables = Disposables(mutableListOf())

    private val totalLifeCycleTypes: List<LifeCycleTypes> = listOf(singletons, disposables)

    private val singletonRegister: SingletonRegister = SingletonRegister(singletons)
    private val disposableRegister: DisposableRegister = DisposableRegister(disposables)

    internal fun <T : Any> registerSingleton(qualifier: String? = null, registerBlock: () -> T) {
        singletonRegister.register(qualifier, registerBlock)
    }

    internal fun <T : Any> registerDisposable(qualifier: String? = null, registerBlock: () -> T) {
        disposableRegister.register(qualifier, registerBlock)
    }

    internal fun <T : Any> search(kType: KType, qualifier: String?): LifeCycleType<T>? {
        when (qualifier.isNullOrEmpty()) {
            true -> {
                totalLifeCycleTypes.forEach {
                    val eachLifeCyclesSearchResult: LifeCycleType<T>? =
                        it.searchWithOutQualifier<T>(kType)
                    if (eachLifeCyclesSearchResult != null) return eachLifeCyclesSearchResult
                }
            }
            false -> {
                totalLifeCycleTypes.forEach {
                    val eachLifeCyclesSearchResult: LifeCycleType<T>? =
                        it.searchWithQualifier<T>(kType, qualifier)
                    if (eachLifeCyclesSearchResult != null) return eachLifeCyclesSearchResult
                }
            }
        }
        return null
    }
}
