package com.example.di.application

import android.app.Application
import android.content.Context
import com.example.di.container.DiActivityModuleContainer
import com.example.di.module.ApplicationModule
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

open class DiApplication(private val applicationModuleClazz: Class<out ApplicationModule>) :
    Application() {
    private lateinit var applicationModule: ApplicationModule
    lateinit var diContainer: DiActivityModuleContainer
        private set

    override fun onCreate() {
        super.onCreate()
        val primaryConstructor = applicationModuleClazz.kotlin.primaryConstructor
            ?: throw NullPointerException("ApplicationModule은 매개변수가 없는 주생성자가 있어야 합니다")
        validateApplicationModulePrimaryConstructor(primaryConstructor)
        applicationModule = primaryConstructor.call(this)
        diContainer = DiActivityModuleContainer(applicationModule)
    }

    private fun validateApplicationModulePrimaryConstructor(primaryConstructor: KFunction<ApplicationModule>) {
        check(primaryConstructor.valueParameters.size == 1) {
            ERROR_APPLICATION_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
        }
        check(primaryConstructor.valueParameters.first().type.jvmErasure == Context::class) {
            ERROR_APPLICATION_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
        }
    }

    companion object {
        private const val ERROR_APPLICATION_MODULE_PRIMARY_CONSTRUCTOR_CONDITION =
            "[ERROR] ApplicationModule를 상속한 클래스의 생성자의 매개변수는 1개여야 하고, 그 타입은 Context여야 합니다."
    }
}
