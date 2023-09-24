package com.example.di.module

import android.content.Context
import com.example.di.application.DiApplication
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

abstract class ApplicationModule(val applicationContext: Context) : Module() {
    fun inject(application: DiApplication) { // 액티비티에 필드 주입해주는 메소드
        provideInjectField(application)
    }

    companion object {
        private val VIEW_MODEL_MODULE_VALUE_PARAMETER_TYPES =
            ApplicationModule::class.primaryConstructor?.valueParameters?.map { it.type.jvmErasure }
                ?: emptyList()

        fun <T : ApplicationModule> validatePrimaryConstructor(moduleClassType: Class<T>): Boolean {
            val primaryConstructor = moduleClassType.kotlin.primaryConstructor ?: return false
            if (primaryConstructor.valueParameters.size == VIEW_MODEL_MODULE_VALUE_PARAMETER_TYPES.size &&
                primaryConstructor.valueParameters.map { it.type.jvmErasure } == VIEW_MODEL_MODULE_VALUE_PARAMETER_TYPES
            ) {
                return true
            }
            return false
        }
    }
}
