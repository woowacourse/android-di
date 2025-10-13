package com.shopping.di.definition

import kotlin.reflect.KClass

/**
 * 기록된 정보를 찾기 위한 key 역할
 *
 * @property kclass 조회 하려는 객체의 클래스 타입
 * @property qualifier 객체 구별용 추가 식별자
 */
internal data class DefinitionKey(
    val kclass: KClass<*>,
    val qualifier: Qualifier? = null,
)
