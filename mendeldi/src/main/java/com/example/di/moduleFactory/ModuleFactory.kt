package com.example.di.moduleFactory

import android.content.Context
import com.example.di.activity.DiEntryPointActivity
import com.example.di.container.DiActivityRetainedModuleContainer
import com.example.di.module.ActivityModule
import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ApplicationModule
import com.example.di.module.ViewModelModule
import kotlin.reflect.full.primaryConstructor

class ModuleFactory(
    private val applicationModuleClazz: Class<out ApplicationModule>,
    activityRetainedModuleClazz: Class<out ActivityRetainedModule>,
    private val viewModelModuleClazz: Class<out ViewModelModule>,
    private val activityModuleClazz: Class<out ActivityModule>,
) {
    private val diActivityRetainedContainer =
        DiActivityRetainedModuleContainer(activityRetainedModuleClazz)

    init {
        require(ApplicationModule.validatePrimaryConstructor(applicationModuleClazz)) {
            "[ERROR] ApplicationModule를 상속한 클래스의 생성자의 매개변수가 조건에 일치하지 않습니다."
        }
        require(ActivityRetainedModule.validatePrimaryConstructor(activityRetainedModuleClazz)) {
            "[ERROR] ActivityRetainedModule을 상속한 클래스의 생성자의 매개변수가 조건에 일치하지 않습니다."
        }
        require(ViewModelModule.validatePrimaryConstructor(viewModelModuleClazz)) {
            "[ERROR] ViewModelModule을 상속한 클래스의 생성자의 매개변수가 조건에 일치하지 않습니다."
        }
        require(ActivityModule.validatePrimaryConstructor(activityModuleClazz)) {
            "[ERROR] ActivityModule을 상속한 클래스의 생성자의 매개변수가 조건에 일치하지 않습니다."
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ApplicationModule> getApplicationModule(
        applicationContext: Context,
    ): T {
        val primaryConstructor = applicationModuleClazz.kotlin.primaryConstructor
            ?: throw NullPointerException("[ERROR] 주생성자가 존재하지 않습니다")
        return primaryConstructor.call(applicationContext) as T
    }

    fun <T : ActivityRetainedModule> getActivityRetainedModule(
        activity: DiEntryPointActivity,
        oldOwnerHashCode: Int?,
        applicationModule: ApplicationModule,
    ): T {
        return diActivityRetainedContainer.provideActivityRetainedModule(
            activity,
            oldOwnerHashCode,
            applicationModule,
        ) as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ActivityModule> getActivityModule(
        activity: DiEntryPointActivity,
        activityRetainedModule: ActivityRetainedModule,
    ): T {
        val primaryConstructor = activityModuleClazz.kotlin.primaryConstructor
            ?: throw NullPointerException("[ERROR] 주생성자가 존재하지 않습니다")
        return primaryConstructor.call(activity, activityRetainedModule) as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ViewModelModule> getViewModelModule(
        activityRetainedModule: ActivityRetainedModule,
    ): T {
        val primaryConstructor = viewModelModuleClazz.kotlin.primaryConstructor
            ?: throw NullPointerException("[ERROR] 주생성자가 존재하지 않습니다")
        return primaryConstructor.call(activityRetainedModule) as T
    }
}
