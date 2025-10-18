package com.example.di.di

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
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
    private val scopedInstances = mutableMapOf<Any, MutableMap<KClass<*>, Any>>()

    // 자동 cleanup을 위한 LifecycleObservers
    private val lifecycleObservers = mutableMapOf<Any, LifecycleEventObserver>()

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

    // 스코프, Observer 정리
    fun clearScope(scopeHolder: Any) {
        Log.d("AppContainer", "clearScope: $scopeHolder")
        scopedInstances.remove(scopeHolder)
        lifecycleObservers.remove(scopeHolder)?.let { observer ->
            if (scopeHolder is LifecycleOwner) {
                scopeHolder.lifecycle.removeObserver(observer)
            }
        }
    }

    // 생명주기 별 Observer 등록
    fun registerScopeHolder(scopeHolder: Any) {
        when (scopeHolder) {
            is LifecycleOwner -> {
                val observer =
                    LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            clearScope(scopeHolder)
                        }
                    }
                scopeHolder.lifecycle.addObserver(observer)
                lifecycleObservers[scopeHolder] = observer
            }

            is ViewModel -> {
                scopeHolder.addCloseable {
                    clearScope(scopeHolder)
                }
            }

            else -> {
                throw IllegalArgumentException("${scopeHolder}는 지원하지 않는 scopeHolder입니다.")
            }
        }
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
                validateScopeHolder(scopeHolder, scopeType)
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
        scopeHolder: Any,
    ): T {
        val scopeMap: MutableMap<KClass<*>, Any> =
            scopedInstances.getOrPut(scopeHolder) {
                registerScopeHolder(scopeHolder)
                mutableMapOf()
            }

        scopeMap[clazz]?.let {
            return it as T
        }

        val instance = createInstance(clazz, scopeHolder)
        scopeMap[clazz] = instance
        return instance
    }

    private fun <T : Any> createInstance(
        clazz: KClass<T>,
        scopeHolder: Any?,
    ): T {
        val constructor: KFunction<T> =
            clazz.primaryConstructor
                ?: throw IllegalStateException("${clazz}의 주생성자를 찾을 수 없습니다.")
        val args: List<Any> =
            constructor.parameters.map { param ->
                val paramClass =
                    param.type.classifier as? KClass<*>
                        ?: throw IllegalStateException("유효하지 않은 파라미터 타입입니다.")

                if (paramClass == Context::class && scopeHolder is Context) {
                    scopeHolder
                } else {
                    getInstance(paramClass, scopeHolder) // 재귀적으로 의존성 생성
                }
            }
        return constructor.call(*args.toTypedArray())
    }

    private fun validateScopeHolder(
        scopeHolder: Any,
        scopeType: ScopeType,
    ) {
        when (scopeType) {
            ScopeType.ACTIVITY_SCOPED -> {
                if (scopeHolder !is Activity) {
                    throw IllegalArgumentException(
                        "ACTIVITY_SCOPED는 Activity ScopeHolder를 받아야 합니다. ${scopeHolder::class.simpleName}",
                    )
                }
            }

            ScopeType.VIEWMODEL_SCOPED -> {
                if (scopeHolder !is ViewModel) {
                    throw IllegalArgumentException(
                        "VIEWMODEL_SCOPED는 ViewModel ScopeHolder를 받아야 합니다. ${scopeHolder::class.simpleName}",
                    )
                }
            }

            ScopeType.SINGLETON -> {
                // Singleton doesn't require specific scope holder type
            }
        }
    }
}
