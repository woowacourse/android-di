package com.shopping.di.definition

/**
 *  동일한 인터페이스 구현체를 구분하기 위한 인터페이스
 */
sealed interface Qualifier {
    /**
     * @property name 구현체를 식별할 문자열
     */
    data class Named(
        val name: String,
    ) : Qualifier
}
