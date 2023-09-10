package com.angrypig.autodi

import androidx.lifecycle.ViewModel
import kotlin.reflect.KType

class ViewModelBundle<VM : ViewModel>(
    private val initializeMethod: () -> VM,
) {
    val type: KType =
        getLambdaReturnType(initializeMethod) ?: throw IllegalStateException(KTYPE_NULL_ERROR)

    fun getInstance(): VM = initializeMethod()

    companion object {
        private const val KTYPE_NULL_ERROR = "뷰모델 타입값이 Null 로 입력되었습니다."
    }
}
