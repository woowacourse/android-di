package com.example.di.module

import android.content.Context
import com.example.di.application.DiApplication
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.jvmName

abstract class ApplicationModule(val applicationContext: Context) : Module() {
    fun inject(application: DiApplication) { // 액티비티에 필드 주입해주는 메소드
        provideInjectField(application)
    }

    companion object {
        private val VIEW_MODEL_MODULE_VALUE_PARAMETER_TYPES =
            ApplicationModule::class.primaryConstructor?.valueParameters?.map { it.type.jvmErasure }
                ?: emptyList()
        private val ERROR_APPLICATION_MODULE_PRIMARY_CONSTRUCTOR_CONDITION =
            "[ERROR] ApplicationModule를 상속한 클래스의 생성자의 매개변수는 ${VIEW_MODEL_MODULE_VALUE_PARAMETER_TYPES.size}개여야 하고, " +
                "그 타입은 ${VIEW_MODEL_MODULE_VALUE_PARAMETER_TYPES.joinToString { it.jvmName }}여야 합니다."

        fun <T : ApplicationModule> validatePrimaryConstructor(moduleClassType: Class<T>): KFunction<T> {
            val primaryConstructor = moduleClassType.kotlin.primaryConstructor
                ?: throw NullPointerException("[ERROR] 주생성자가 존재하지 않습니다")
            check(primaryConstructor.valueParameters.size == VIEW_MODEL_MODULE_VALUE_PARAMETER_TYPES.size) {
                ERROR_APPLICATION_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
            }
            check(primaryConstructor.valueParameters.map { it.type.jvmErasure } == VIEW_MODEL_MODULE_VALUE_PARAMETER_TYPES) {
                ERROR_APPLICATION_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
            }
            return primaryConstructor
        }
    }
}
