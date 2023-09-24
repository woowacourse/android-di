package com.example.di.application

import android.app.Application
import com.example.di.activity.DiEntryPointActivity
import com.example.di.module.ActivityModule
import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ApplicationModule
import com.example.di.module.ViewModelModule
import com.example.di.moduleFactory.ModuleFactory

abstract class DiApplication(
    applicationModuleClazz: Class<out ApplicationModule>,
    activityRetainedModuleClazz: Class<out ActivityRetainedModule>,
    viewModelModuleClazz: Class<out ViewModelModule>,
    activityModuleClazz: Class<out ActivityModule>,
) : Application() {
    private val moduleFactory = ModuleFactory(
        applicationModuleClazz,
        activityRetainedModuleClazz,
        viewModelModuleClazz,
        activityModuleClazz,
    )
    private lateinit var applicationModule: ApplicationModule

    override fun onCreate() {
        super.onCreate()
        applicationModule = moduleFactory.getApplicationModule(this)
        applicationModule.inject(this) // DiApplication을 상속 구현한 클래스가 필드 주입이 필요하다면, 여기서 해줌.
    }

    fun <T : ActivityRetainedModule> getActivityRetainedModule(
        activity: DiEntryPointActivity,
        oldOwnerHashCode: Int?,
    ): T {
        return moduleFactory.getActivityRetainedModule(
            activity,
            oldOwnerHashCode,
            applicationModule,
        )
    }

    fun <T : ActivityModule> getActivityModule(
        activity: DiEntryPointActivity,
        activityRetainedModule: ActivityRetainedModule,
    ): T {
        return moduleFactory.getActivityModule(activity, activityRetainedModule)
    }

    fun <T : ViewModelModule> getViewModelModule(
        activityRetainedModule: ActivityRetainedModule,
    ): T {
        return moduleFactory.getViewModelModule(activityRetainedModule)
    }
}
