package com.example.di.moduleFactory

import android.content.Context
import com.example.di.activity.DiEntryPointActivity
import com.example.di.container.DiActivityRetainedModuleContainer
import com.example.di.module.ActivityModule
import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ApplicationModule
import com.example.di.module.ViewModelModule

class ModuleFactory(
    private val applicationModuleClazz: Class<out ApplicationModule>,
    activityRetainedModuleClazz: Class<out ActivityRetainedModule>,
    private val viewModelModuleClazz: Class<out ViewModelModule>,
    private val activityModuleClazz: Class<out ActivityModule>,
) {
    private val diActivityRetainedContainer =
        DiActivityRetainedModuleContainer(activityRetainedModuleClazz)

    @Suppress("UNCHECKED_CAST")
    fun <T : ApplicationModule> getApplicationModule(
        applicationContext: Context,
    ): T {
        val primaryConstructor = ApplicationModule.getPrimaryConstructor(applicationModuleClazz)
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
        val primaryConstructor = ActivityModule.getPrimaryConstructor(activityModuleClazz)
        return primaryConstructor.call(activity, activityRetainedModule) as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ViewModelModule> getViewModelModule(
        activityRetainedModule: ActivityRetainedModule,
    ): T {
        val primaryConstructor = ViewModelModule.getPrimaryConstructor(viewModelModuleClazz)
        return primaryConstructor.call(activityRetainedModule) as T
    }
}
