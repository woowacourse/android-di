package com.yrsel.di

import kotlin.reflect.KClass

/**
 * 기록된 정보를 찾기 위한 key 역할
 *
 * @property kclass 조회 하려는 객체의 클래스 타입
 * @property qualifier 커스텀 Annotation의 클래스 (예: Memory::class)
 */
internal data class DefinitionKey(
    val kclass: KClass<*>,
    val qualifier: KClass<out Annotation>? = null,
)
