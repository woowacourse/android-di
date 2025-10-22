package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AppContainer(
    private val bindings: Map<DIKey, () -> Any> = emptyMap(),
) {
    private val singletonCache = mutableMapOf<DIKey, Any>()
    private val activityCache = mutableMapOf<DIKey, Any>()
    private val viewModelCache = mutableMapOf<DIKey, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(
        clazz: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        scope: Scope = Scope.Singleton,
    ): T {
        val key = DIKey(clazz, qualifier)
        return when (scope) {
            Scope.Singleton -> singletonCache.getOrPut(key) { createInstance(key) } as T
            Scope.Activity -> activityCache.getOrPut(key) { createInstance(key) } as T
            Scope.ViewModel -> viewModelCache.getOrPut(key) { createInstance(key) } as T
        }
    }

    private fun createInstance(key: DIKey): Any {
        // 바인딩이 있는 경우
        bindings[key]?.let { return it() }

        // 주 생성자가 있는지 확인
        val constructor =
            key.clazz.primaryConstructor
                ?: throw IllegalStateException(
                    "${key.clazz.simpleName} 클래스의 인스턴스를 생성할 수 없습니다. " +
                        "바인딩이 제공되지 않았고, 주 생성자를 찾을 수 없습니다.",
                )

        // 파라미터가 없는 주 생성자면 호출
        if (constructor.parameters.isEmpty()) {
            return constructor.call()
        }

        // 파라미터가 필요한 주 생성자면 실패
        throw IllegalStateException(
            "${key.clazz.simpleName} 클래스의 인스턴스를 생성할 수 없습니다. " +
                "주 생성자가 매개변수를 필요로 합니다: ${constructor.parameters.map { it.name }}",
        )
    }

    fun clearActivityScope() {
        activityCache.clear()
    }
}
