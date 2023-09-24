package com.example.di.module

import android.content.Context
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.jvmName

// 액티비티가 onDestory되어도 구성변경에 살아남는 모듈임. 때문에, 메모리 누수 방지를 위해 액티비티 컨텍스트를 가질 수 없음.
abstract class ActivityRetainedModule(applicationModule: ApplicationModule) :
    Module(applicationModule) {
    val applicationContext: Context = applicationModule.applicationContext

    companion object {
        private val ACTIVITY_RETAINED_MODULE_VALUE_PARAMETER_TYPES =
            ActivityRetainedModule::class.primaryConstructor?.valueParameters?.map { it.type.jvmErasure }
                ?: emptyList()
        private val ERROR_ACTIVITY_RETAINED_MODULE_PRIMARY_CONSTRUCTOR_CONDITION =
            "[ERROR] ActivityRetainedModule의 상속한 클래스의 생성자의 매개변수는 ${ACTIVITY_RETAINED_MODULE_VALUE_PARAMETER_TYPES.size}개여야 하고, 그 타입은 ${
                ACTIVITY_RETAINED_MODULE_VALUE_PARAMETER_TYPES.joinToString(",") { it.jvmName }
            }이어야 합니다."

        fun <T : ActivityRetainedModule> getPrimaryConstructor(moduleClassType: Class<T>): KFunction<T> {
            val primaryConstructor = moduleClassType.kotlin.primaryConstructor
                ?: throw NullPointerException("[ERROR] 주생성자가 존재하지 않습니다")
            validatePrimaryConstructor(primaryConstructor)
            return primaryConstructor
        }

        private fun <T : ActivityRetainedModule> validatePrimaryConstructor(primaryConstructor: KFunction<T>) {
            check(primaryConstructor.valueParameters.size == ACTIVITY_RETAINED_MODULE_VALUE_PARAMETER_TYPES.size) {
                ERROR_ACTIVITY_RETAINED_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
            }
            check(primaryConstructor.valueParameters.map { it.type.jvmErasure } == ACTIVITY_RETAINED_MODULE_VALUE_PARAMETER_TYPES) {
                ERROR_ACTIVITY_RETAINED_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
            }
        }
    }
}
