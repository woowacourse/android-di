package com.bandal.fullmoon

sealed class DIError(override val message: String?) : Throwable() {
    class NotFoundPrimaryConstructor : DIError("주 생성자를 찾을 수 없습니다.")

    class NotFoundCreateFunction(key: String) :
        DIError("[키: $key] 인스턴스를 만들 함수를 찾을 수 없습니다.")
}
