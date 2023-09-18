package com.angrypig.autodi

import kotlin.reflect.KType

sealed class LifeCycleType<T : Any> {
    abstract fun getInstance(): T
    abstract val qualifier: String?
    abstract val type: KType
    abstract fun override(initializeMethod: () -> T)

    class Singleton<T : Any>(
        override val qualifier: String? = null,
        private var initializeMethod: () -> T,
    ) : LifeCycleType<T>() {
        private lateinit var instance: T
        override val type: KType =
            getLambdaReturnType(initializeMethod) ?: throw IllegalStateException(KTYPE_NULL_ERROR)

        override fun getInstance(): T {
            if (!::instance.isInitialized) instance = initializeMethod()
            return instance
        }

        override fun override(initializeMethod: () -> T) {
            this.initializeMethod = initializeMethod
            instance = initializeMethod()
        }
    }

    class Disposable<T : Any>(
        override val qualifier: String? = null,
        private var initializeMethod: () -> T,
    ) : LifeCycleType<T>() {
        override val type: KType =
            getLambdaReturnType(initializeMethod) ?: throw IllegalStateException(KTYPE_NULL_ERROR)

        override fun getInstance(): T {
            return initializeMethod()
        }

        override fun override(initializeMethod: () -> T) {
            this.initializeMethod = initializeMethod
        }
    }

//    class Activity<T : Any> : LifeCycleType<T>
//
//    class Fragment<T : Any> : LifeCycleType<T>

    companion object {
        private const val KTYPE_NULL_ERROR = "인스턴스의 타입값이 Null 로 입력되었습니다."
    }
}
