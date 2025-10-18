package com.example.di.di

import android.util.Log
import com.example.di.annotation.Scope
import com.example.di.annotation.ScopeType
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class AppContainer {
    // 싱글턴 인스턴스 캐싱
    private val singletonInstances = mutableMapOf<KClass<*>, Any>()

    // 스코프별 인스턴스 캐싱
    private val scopedInstances = mutableMapOf<String, MutableMap<KClass<*>, Any>>()

    // TODO 디버깅용 코드 제거
    fun showInstances() {
        Log.d("AppContainer", "singletonInstances: $singletonInstances")
        singletonInstances.forEach {
            Log.d("AppContainer", "singletonInstances: ${it.key.simpleName} = ${it.value}")
        }
        Log.d("AppContainer", "scopedInstances: $scopedInstances")
        scopedInstances.forEach {
            Log.d("AppContainer", "scopedInstances: ${it.key} = ${it.value}")
        }
    }

    // 스코프 정리
    fun clearScope(scopeHolder: Any) {
        Log.d("AppContainer", "clearScope: $scopeHolder")
        scopedInstances.remove(scopeHolder)
    }

    fun <T : Any> registerSingleton(singleton: T) {
        // Database와 같은 싱글턴 객체 캐싱
        val clazz = singleton::class
        singletonInstances[clazz] = singleton

        // DAO 함수 탐색
        clazz.declaredFunctions
            .filter { function: KFunction<*> ->
                function.name.endsWith("Dao") && function.parameters.size == 1
            }.forEach { daoFunction: KFunction<*> ->
                // returnType: Dao의 interface
                val returnType =
                    daoFunction.returnType.classifier as? KClass<*>
                        ?: throw IllegalStateException("유효하지 않은 반환 타입입니다.")
                val daoInstance: Any? = daoFunction.call(singleton)
                if (daoInstance != null) {
                    singletonInstances[returnType] = daoInstance
                }
            }
    }

    fun <T : Any> getInstance(
        clazz: KClass<T>,
        scopeHolder: Any? = null,
        scopeType: ScopeType? = null,
    ): T {
        // 명시된 Scope가 없으면 클래스의 Scope를 사용
        val scopeType: ScopeType? = scopeType ?: clazz.findAnnotation<Scope>()?.value

        return when (scopeType) {
            ScopeType.SINGLETON -> getSingletonInstance(clazz)
            ScopeType.ACTIVITY_SCOPED,
            ScopeType.VIEWMODEL_SCOPED,
            -> {
                if (scopeHolder == null) {
                    throw IllegalStateException("${clazz.simpleName}의 scopeHolder가 제공되지 않았습니다.")
                }
                getScopedInstance(clazz, scopeHolder)
            }

            // 어노테이션이 없으면 기본적으로 싱글턴으로 처리
            else -> getSingletonInstance(clazz)
        }
    }

    fun <T : Any> getSingletonInstance(clazz: KClass<T>): T {
        // 이미 있는 인스턴스면 반환
        singletonInstances[clazz]?.let {
            return it as T
        }

        // 없으면 인스턴스 생성
        val instance = createInstance(clazz, null)
        singletonInstances[clazz] = instance

        return instance
    }

    fun <T : Any> getScopedInstance(
        clazz: KClass<T>,
        scopeId: Any,
    ): T {
        val scopeMap: MutableMap<KClass<*>, Any> =
            scopedInstances.getOrPut(clazz.qualifiedName ?: "") { mutableMapOf() }

        scopeMap[clazz]?.let {
            return it as T
        }

        val instance = createInstance(clazz, scopeId)
        scopeMap[clazz] = instance
        return instance
    }

    private fun <T : Any> createInstance(
        clazz: KClass<T>,
        scopeHolder: Any?,
    ): T {
        Log.d("AppContainer", "createInstance: $clazz")
        val constructor: KFunction<T> =
            clazz.primaryConstructor ?: throw IllegalStateException("주생성자를 찾을 수 없습니다.")
        val args: List<Any> =
            constructor.parameters.map { param ->
                val paramClass =
                    param.type.classifier as? KClass<*>
                        ?: throw IllegalStateException("유효하지 않은 파라미터 타입입니다.")
                getInstance(paramClass, scopeHolder) // 재귀적으로 의존성 생성
            }
        return constructor.call(*args.toTypedArray())
    }
}
