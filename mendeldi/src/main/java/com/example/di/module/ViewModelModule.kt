package com.example.di.module

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.jvmName

// 뷰모델 모듈도 액티비티보다 생명주기가 길기 때문에, 액티비티 컨텍스트를 참조하지 못한다.
abstract class ViewModelModule(activityRetainedModule: ActivityRetainedModule) :
    Module(activityRetainedModule) {
    val applicationContext: Context = activityRetainedModule.applicationContext

    fun <VM : ViewModel> createViewModel(clazz: Class<VM>): VM {
        return provideInstance(clazz)
    }

    companion object {
        private val VIEW_MODEL_MODULE_VALUE_PARAMETER_TYPES =
            ViewModelModule::class.primaryConstructor?.valueParameters?.map { it.type.jvmErasure }
                ?: emptyList()
        private val ERROR_VIEW_MODEL_MODULE_PRIMARY_CONSTRUCTOR_CONDITION =
            "[ERROR] ViewModelModule을 상속받은 클래스의 생성자는 ${VIEW_MODEL_MODULE_VALUE_PARAMETER_TYPES.size}개의 인자로 ${
                VIEW_MODEL_MODULE_VALUE_PARAMETER_TYPES.joinToString(separator = ",") { it.jvmName }
            } 을 가져야 합니다."

        fun <T : ViewModelModule> validatePrimaryConstructor(moduleClassType: Class<T>): KFunction<T> {
            val primaryConstructor = moduleClassType.kotlin.primaryConstructor
                ?: throw NullPointerException("[ERROR] 주생성자가 존재하지 않습니다")
            check(primaryConstructor.valueParameters.size == VIEW_MODEL_MODULE_VALUE_PARAMETER_TYPES.size) {
                ERROR_VIEW_MODEL_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
            }
            check(primaryConstructor.valueParameters.map { it.type.jvmErasure } == VIEW_MODEL_MODULE_VALUE_PARAMETER_TYPES) {
                ERROR_VIEW_MODEL_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
            }
            return primaryConstructor
        }
    }
}
