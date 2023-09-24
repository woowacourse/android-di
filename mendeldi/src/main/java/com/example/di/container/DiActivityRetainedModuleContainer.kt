package com.example.di.container

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.di.activity.DiEntryPointActivity
import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ApplicationModule
import kotlin.reflect.full.primaryConstructor

class DiActivityRetainedModuleContainer(
    private val activityRetainedModuleClazz: Class<out ActivityRetainedModule>,
) : DefaultLifecycleObserver {
    private val moduleMap: MutableMap<Int, ActivityRetainedModule> = mutableMapOf()

    init {
        require(ActivityRetainedModule.validatePrimaryConstructor(activityRetainedModuleClazz)) {
            "[ERROR] ActivityRetainedModule을 상속한 클래스의 생성자의 매개변수가 조건에 일치하지 않습니다."
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ActivityRetainedModule> provideActivityRetainedModule(
        activity: DiEntryPointActivity,
        oldOwnerHashCode: Int?,
        applicationModule: ApplicationModule,
    ): T {
        val module = moduleMap.remove(oldOwnerHashCode)
            ?: createFromOwnerHashCode(applicationModule)
        moduleMap[activity.hashCode()] = module
        activity.lifecycle.addObserver(this)
        return module as T
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : ActivityRetainedModule> createFromOwnerHashCode(
        applicationModule: ApplicationModule,
    ): T {
        val primaryConstructor = activityRetainedModuleClazz.kotlin.primaryConstructor
            ?: throw NullPointerException("[ERROR] 주생성자가 존재하지 않습니다")
        return primaryConstructor.call(applicationModule) as T
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (owner is DiEntryPointActivity) {
            if (owner.isFinishing) moduleMap.remove(owner.hashCode())
        }
    }
}
