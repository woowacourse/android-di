package com.angrypig.autodi.autoDIModule.register

import com.angrypig.autodi.autoDIModule.LifeCycleTypes

class SingletonRegister(private val singletons: LifeCycleTypes.Singletons) {
    internal fun <T : Any> register(qualifier: String? = null, initializeMethod: () -> T) {
        singletons.add(qualifier, initializeMethod)
    }
}
