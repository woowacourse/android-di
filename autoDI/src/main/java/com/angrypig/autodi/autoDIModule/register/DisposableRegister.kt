package com.angrypig.autodi.autoDIModule.register

import com.angrypig.autodi.autoDIModule.LifeCycleTypes

class DisposableRegister(private val disposables: LifeCycleTypes.Disposables) {
    internal fun <T : Any> register(qualifier: String? = null, initializeMethod: () -> T) {
        disposables.add(qualifier, initializeMethod)
    }
}
