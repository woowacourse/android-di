package com.example.di.application

import android.app.Application
import com.example.di.container.DiActivityRetainedModuleContainer
import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ApplicationModule

open class DiApplication(private val applicationModuleClazz: Class<out ApplicationModule>) :
    Application() {
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

    fun <T : ActivityRetainedModule> getActivityRetainedModule(
        newOwnerHashCode: Int,
        oldOwnerHashCode: Int?,
        clazz: Class<T>,
    ): T {
        return diActivityRetainedContainer.provideActivityRetainedModule(
            newOwnerHashCode,
            oldOwnerHashCode,
            clazz,
        )
    }

    fun removeActivityRetainedModule(ownerHashCode: Int) {
        diActivityRetainedContainer.removeModule(ownerHashCode)
    }
}
