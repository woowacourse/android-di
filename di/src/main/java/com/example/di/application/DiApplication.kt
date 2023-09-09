package com.example.di.application

import android.app.Application
import com.example.di.container.DiActivityModuleContainer
import com.example.di.module.ApplicationModule
import kotlin.reflect.full.primaryConstructor

open class DiApplication(private val applicationModuleClazz: Class<out ApplicationModule>) :
    Application() {
    private lateinit var applicationModule: ApplicationModule
    lateinit var diContainer: DiActivityModuleContainer
        private set

    override fun onCreate() {
        super.onCreate()
        val primaryConstructor = applicationModuleClazz.kotlin.primaryConstructor
            ?: throw NullPointerException("ApplicationModule은 매개변수가 없는 주생성자가 있어야 합니다")
        applicationModule = primaryConstructor.call(this)
        diContainer = DiActivityModuleContainer(applicationModule)
    }
}
