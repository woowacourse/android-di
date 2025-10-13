package com.shopping.di.definition

enum class Kind {
    /**
     * 앱 실행 후, 한 번만 인스턴스 생성
     */
    SINGLETON,

    /**
     * 호출 마다 새로운 인스턴스 생성
     */
    FACTORY,
}
