package com.bandal.di

import kotlin.reflect.KClass

sealed class DIError(override val message: String?) : Throwable() {
    class NotFoundPrimaryConstructor : DIError("주 생성자를 찾을 수 없습니다.")
    class NotFoundInstanceForInject(type: KClass<*>) : DIError("[필요 타입: $type] 의존성 주입에 필요한 인스턴스를 찾을 수 없습니다.")
}
