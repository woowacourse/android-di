package com.example.di.module

import android.content.Context
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

// 액티비티가 onDestory되어도 구성변경에 살아남는 모듈임. 때문에, 메모리 누수 방지를 위해 액티비티 컨텍스트를 가질 수 없음.
abstract class ActivityRetainedModule(applicationModule: ApplicationModule) :
    Module(applicationModule) {
    val applicationContext: Context = applicationModule.applicationContext

    companion object {
        private val ACTIVITY_RETAINED_MODULE_VALUE_PARAMETER_TYPES =
            ActivityRetainedModule::class.primaryConstructor?.valueParameters?.map { it.type.jvmErasure }
                ?: emptyList()

        fun <T : ActivityRetainedModule> validatePrimaryConstructor(moduleClassType: Class<T>): Boolean {
            val primaryConstructor = moduleClassType.kotlin.primaryConstructor ?: return false
            if (primaryConstructor.valueParameters.size == ACTIVITY_RETAINED_MODULE_VALUE_PARAMETER_TYPES.size &&
                primaryConstructor.valueParameters.map { it.type.jvmErasure } == ACTIVITY_RETAINED_MODULE_VALUE_PARAMETER_TYPES
            ) {
                return true
            }
            return false
        }
    }
}
