package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AppContainer {
    private val providers = mutableMapOf<Pair<KClass<*>, KClass<out Annotation>?>, Any>()
    private val typeBindings = mutableMapOf<Pair<KClass<*>, KClass<out Annotation>?>, KClass<*>>()

    // 인터페이스와 구현체를 연결(클래스 바인딩)
    fun <T : Any> bind(
        base: KClass<T>,
        impl: KClass<out T>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        typeBindings[base to qualifier] = impl
    }

    // 이미 생성된 인스턴스를 직접 등록(싱글톤 객체 등)
    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        providers[type to qualifier] = instance
    }

    // 등록된 규칙에 따라 의존성을 찾아 생성 및 반환
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        clazz: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        // 생성된 인스턴스가 있는 지 찾기
        val key = clazz to qualifier
        providers[key]?.let { return it as T }

        val implementationClass = (typeBindings[key] ?: typeBindings[clazz to null] ?: clazz) as KClass<T>
        val primaryConstructor =
            implementationClass.primaryConstructor
                ?: implementationClass.constructors.firstOrNull()
                ?: throw IllegalArgumentException("${implementationClass.simpleName}는 주 생성자가 없습니다")

        val constructorArguments =
            primaryConstructor.parameters.associateWith { param ->
                val depClass = param.type.classifier as KClass<*>
                val depQualifier = param.annotations.firstOrNull()?.annotationClass
                get(depClass, depQualifier)
            }

        // 캐싱 후 반환
        val instance = primaryConstructor.callBy(constructorArguments)
        providers[key] = instance

        return instance
    }
}
