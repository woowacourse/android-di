package com.example.di.application

import android.app.Application
import com.example.di.container.DiActivityRetainedModuleContainer
import com.example.di.module.ActivityModule
import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ApplicationModule
import com.example.di.module.ViewModelModule

open class DiApplication(
    private val applicationModuleClazz: Class<out ApplicationModule>,
    private val activityRetainedModuleClazz: Class<out ActivityRetainedModule>,
    private val activityModuleClazz: Class<out ActivityModule>,
    private val viewModelModuleClazz: Class<out ViewModelModule>,
) : Application() {
    private lateinit var applicationModule: ApplicationModule
    private lateinit var diActivityRetainedContainer: DiActivityRetainedModuleContainer

    override fun onCreate() {
        super.onCreate()
        val primaryConstructor =
            ApplicationModule.validatePrimaryConstructor(applicationModuleClazz)
        applicationModule = primaryConstructor.call(this)
        applicationModule.inject(this) // DiApplication을 상속 구현한 클래스가 필드 주입이 필요하다면, 여기서 해줌.
        diActivityRetainedContainer = DiActivityRetainedModuleContainer(applicationModule)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ActivityRetainedModule> getActivityRetainedModule(
        newOwnerHashCode: Int,
        oldOwnerHashCode: Int?,
    ): T {
        return diActivityRetainedContainer.provideActivityRetainedModule(
            newOwnerHashCode,
            oldOwnerHashCode,
            activityRetainedModuleClazz,
        ) as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ActivityModule> getActivityModule(
        activityRetainedModule: ActivityRetainedModule,
    ): T {
        val primaryConstructor =
            ActivityModule.validatePrimaryConstructor(activityModuleClazz)
        return primaryConstructor.call(this, activityRetainedModule) as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ViewModelModule> getViewModelModule(
        activityRetainedModule: ActivityRetainedModule,
    ): T {
        val primaryConstructor =
            ViewModelModule.validatePrimaryConstructor(viewModelModuleClazz)
        return primaryConstructor.call(activityRetainedModule) as T
    }

    fun removeActivityRetainedModule(ownerHashCode: Int) {
        diActivityRetainedContainer.removeModule(ownerHashCode)
    }
}
