package com.example.di.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.primaryConstructor

class AppContainer {
    // 인스턴스 캐싱
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> registerSingleton(singleton: T) {
        // Database와 같은 싱글턴 객체 캐싱
        val clazz = singleton::class
        instances[clazz] = singleton

        // DAO 함수 탐색
        clazz.declaredFunctions
            .filter { function: KFunction<*> ->
                function.name.endsWith("Dao") && function.parameters.size == 1
            }
            .forEach { daoFunction: KFunction<*> ->
                // returnType: Dao의 interface
                val returnType = daoFunction.returnType.classifier as? KClass<*>
                    ?: throw IllegalStateException("유효하지 않은 반환 타입입니다.")
                val daoInstance: Any? = daoFunction.call(singleton)
                if (daoInstance != null) {
                    instances[returnType] = daoInstance
                }
            }
    }

    fun <T : Any> getInstance(clazz: KClass<T>): T {
        // 이미 있는 인스턴스면 반환
        instances[clazz]?.let {
            return it as T
        }

        // 없으면 인스턴스 생성
        val instance = createInstance(clazz)
        instances[clazz] = instance

        return instance
    }

    private fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor: KFunction<T> =
            clazz.primaryConstructor ?: throw IllegalStateException("주생성자를 찾을 수 없습니다.")
        val args: List<Any> = constructor.parameters.map { param ->
            val paramClass = param.type.classifier as? KClass<*>
                ?: throw IllegalStateException("유효하지 않은 파라미터 타입입니다.")
            getInstance(paramClass) // 재귀적으로 의존성 생성
        }
        return constructor.call(*args.toTypedArray())
    }
}
