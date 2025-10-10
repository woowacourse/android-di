package woowacourse.shopping.di.common

import kotlin.reflect.KClass
import kotlin.reflect.KParameter

private const val ERROR_PARAMETER_CAN_NOT_CONVERT_CLASS =
    "parameter -> 클래스 타입 변환 실패, parameterName = %s"

fun KParameter.castKclassOrThrow(): KClass<*> =
    checkNotNull(this.type.classifier as? KClass<*>) {
        ERROR_PARAMETER_CAN_NOT_CONVERT_CLASS.format(this.name)
    }
