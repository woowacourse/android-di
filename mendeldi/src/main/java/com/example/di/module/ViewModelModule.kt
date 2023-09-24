package com.example.di.module

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

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

        fun <T : ViewModelModule> validatePrimaryConstructor(moduleClassType: Class<T>): Boolean {
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
