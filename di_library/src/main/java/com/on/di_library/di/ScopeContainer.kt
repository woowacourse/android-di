package com.on.di_library.di

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.cast

/**
 * 스코프별 인스턴스를 관리하는 컨테이너
 */
object ScopeContainer {
    private val singletonPool: ConcurrentHashMap<KClass<*>, Any> = ConcurrentHashMap()
    private val activityPools: ConcurrentHashMap<Long, ConcurrentHashMap<KClass<*>, Any>> =
        ConcurrentHashMap()
    private val viewModelPools: ConcurrentHashMap<Long, ConcurrentHashMap<KClass<*>, Any>> =
        ConcurrentHashMap()

    /**
     * 스코프에 맞는 인스턴스를 반환하거나 생성
     */
    fun <T : Any> getOrCreate(
        kClass: KClass<T>,
        scopeType: ScopeType,
        scopeId: Long,
        creator: () -> T,
    ): T {
        return when (scopeType) {
            ScopeType.SINGLETON -> {
                kClass.cast(singletonPool.getOrPut(kClass) { creator() })
            }

            ScopeType.ACTIVITY -> {
                val pool = activityPools.getOrPut(scopeId) { ConcurrentHashMap() }
                kClass.cast(pool.getOrPut(kClass) { creator() })
            }

            ScopeType.VIEWMODEL -> {
                val pool = viewModelPools.getOrPut(scopeId) { ConcurrentHashMap() }
                kClass.cast(pool.getOrPut(kClass) { creator() })
            }

            ScopeType.TRANSIENT -> {
                creator()
            }
        }
    }

    /**
     * Activity 스코프 인스턴스 정리
     */
    fun clearActivityScope(activityId: Long) {
        activityPools.remove(activityId)
    }

    /**
     * ViewModel 스코프 인스턴스 정리
     */
    fun clearViewModelScope(viewModelId: Long) {
        viewModelPools.remove(viewModelId)
    }

    /**
     * 모든 스코프 초기화
     */
    fun clearAll() {
        singletonPool.clear()
        activityPools.clear()
        viewModelPools.clear()
    }
}

/**
 * 스코프 타입 정의
 */
enum class ScopeType {
    SINGLETON,   // 앱 전체 생명주기
    ACTIVITY,    // Activity 생명주기
    VIEWMODEL,   // ViewModel 생명주기
    TRANSIENT    // 매번 새로운 인스턴스
}