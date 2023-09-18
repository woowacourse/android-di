package com.example.di.container

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.di.activity.DiEntryPointActivity
import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ApplicationModule

class DiActivityRetainedModuleContainer(
    private val activityRetainedModuleClazz: Class<out ActivityRetainedModule>,
) : DefaultLifecycleObserver {
    private val moduleMap: MutableMap<Int, ActivityRetainedModule> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : ActivityRetainedModule> provideActivityRetainedModule(
        activity: DiEntryPointActivity,
        oldOwnerHashCode: Int?,
        applicationModule: ApplicationModule,
    ): T {
        val module = moduleMap.remove(oldOwnerHashCode) ?: createFromOwnerHashCode(
            activityRetainedModuleClazz,
            applicationModule,
        )
        moduleMap[activity.hashCode()] = module
        activity.lifecycle.addObserver(this)
        return module as T
    }

    private fun <T : ActivityRetainedModule> createFromOwnerHashCode(
        clazz: Class<T>,
        applicationModule: ApplicationModule,
    ): T {
        val primaryConstructor = ActivityRetainedModule.validatePrimaryConstructor(clazz)
        return primaryConstructor.call(applicationModule)
    }

    private fun removeModule(ownerHashCode: Int) {
        moduleMap.remove(ownerHashCode)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (owner is DiEntryPointActivity) {
            if (owner.isFinishing) removeModule(owner.hashCode())
        }
    }
}
