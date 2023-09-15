package com.bandal.di

import kotlin.reflect.KClass

sealed class DIError(override val message: String?) : Throwable() {
    class NotFoundPrimaryConstructor : DIError("주 생성자를 찾을 수 없습니다.")

    class NotFoundQualifierOrInstance(type: KClass<*>) :
        DIError("[타입 : $type] Qualifier나 인스턴스를 찾을 수 없습니다.")
}
