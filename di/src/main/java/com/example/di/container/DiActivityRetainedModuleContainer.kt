package com.example.di.container

import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ApplicationModule

class DiActivityRetainedModuleContainer(private val applicationModule: ApplicationModule) {
    private val moduleMap: MutableMap<Int, ActivityRetainedModule> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : ActivityRetainedModule> provideActivityRetainedModule(
        newOwnerHashCode: Int,
        oldOwnerHashCode: Int?,
        clazz: Class<T>,
    ): T {
        val module = moduleMap.remove(oldOwnerHashCode) ?: createFromOwnerHashCode(clazz)
        moduleMap[newOwnerHashCode] = module
        return module as T
    }

    private fun <T : ActivityRetainedModule> createFromOwnerHashCode(
        clazz: Class<T>,
    ): T {
        val primaryConstructor = ActivityRetainedModule.validatePrimaryConstructor(clazz)
        return primaryConstructor.call(applicationModule)
    }

    fun removeModule(ownerHashCode: Int) {
        moduleMap.remove(ownerHashCode)
    }
}
