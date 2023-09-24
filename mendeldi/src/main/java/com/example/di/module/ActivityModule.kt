package com.example.di.module

import android.content.Context
import com.example.di.activity.DiEntryPointActivity
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.jvmName

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

        private val ERROR_ACTIVITY_MODULE_PRIMARY_CONSTRUCTOR_CONDITION =
            "[ERROR] ActivityModule을 상속받은 클래스의 생성자는 ${ACTIVITY_MODULE_VALUE_PARAMETER_TYPES.size}개의 인자로 ${
                ACTIVITY_MODULE_VALUE_PARAMETER_TYPES.joinToString(separator = ",") { it.jvmName }
            } 을 가져야 합니다."

        fun <T : ActivityModule> getPrimaryConstructor(moduleClassType: Class<T>): KFunction<T> {
            val primaryConstructor = moduleClassType.kotlin.primaryConstructor
                ?: throw NullPointerException("[ERROR] 주생성자가 존재하지 않습니다")
            validatePrimaryConstructor(primaryConstructor)
            return primaryConstructor
        }

        private fun <T : ActivityModule> validatePrimaryConstructor(primaryConstructor: KFunction<T>) {
            check(primaryConstructor.valueParameters.size == ACTIVITY_MODULE_VALUE_PARAMETER_TYPES.size) {
                ERROR_ACTIVITY_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
            }
            check(primaryConstructor.valueParameters.map { it.type.jvmErasure } == ACTIVITY_MODULE_VALUE_PARAMETER_TYPES) {
                ERROR_ACTIVITY_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
            }
        }
    }
}
