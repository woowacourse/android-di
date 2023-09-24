package com.example.di.module

import android.content.Context
import com.example.di.activity.DiEntryPointActivity
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

// 액티비티가 onDestory될 때마다 죽는 모듈임.
abstract class ActivityModule(
    val activityContext: Context, // 액티비티 모듈 안에 정의하는 함수는 액티비티 컨텍스트도 사용이 가능하다.
    activityRetainedModule: ActivityRetainedModule,
) : Module(activityRetainedModule) {
    // 액티비티 모듈 안에 정의하는 함수는 애플리케이션 컨텍스트도 사용이 가능하다.
    val applicationContext: Context = activityRetainedModule.applicationContext

    fun inject(diEntryPointActivity: DiEntryPointActivity) { // 액티비티에 필드 주입해주는 메소드
        provideInjectField(diEntryPointActivity)
    }

    companion object {
        private val ACTIVITY_MODULE_VALUE_PARAMETER_TYPES =
            ActivityModule::class.primaryConstructor?.valueParameters?.map { it.type.jvmErasure }
                ?: emptyList()

        fun <T : ActivityModule> validatePrimaryConstructor(moduleClassType: Class<T>): Boolean {
            val primaryConstructor = moduleClassType.kotlin.primaryConstructor ?: return false
            if (primaryConstructor.valueParameters.size == ACTIVITY_MODULE_VALUE_PARAMETER_TYPES.size &&
                primaryConstructor.valueParameters.map { it.type.jvmErasure } == ACTIVITY_MODULE_VALUE_PARAMETER_TYPES
            ) {
                return true
            }
            return false
        }
    }
}
