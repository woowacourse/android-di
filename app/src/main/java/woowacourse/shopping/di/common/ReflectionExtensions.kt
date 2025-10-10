package woowacourse.shopping.di.common

import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

private const val ERROR_PARAMETER_CAN_NOT_CONVERT_CLASS =
    "parameter -> 클래스 타입 변환 실패, parameterName = %s"
private const val ERROR_VIEWMODEL_NO_PRIMARY_CONSTRUCTOR = "ViewModel 주 생성자가 필요합니다, className = %s"

fun KParameter.castKclassOrThrow(): KClass<*> =
    checkNotNull(this.type.classifier as? KClass<*>) {
        ERROR_PARAMETER_CAN_NOT_CONVERT_CLASS.format(this.name)
    }

fun <T : ViewModel> Class<T>.getPrimaryConstructorOrThrow(): KFunction<T> =
    requireNotNull(this.kotlin.primaryConstructor) {
        ERROR_VIEWMODEL_NO_PRIMARY_CONSTRUCTOR.format(this.name)
    }
