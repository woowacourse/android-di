package woowacourse.shopping.util.autoDI

import kotlin.reflect.KType

sealed class LifeCycleType<T : Any> {
    abstract fun getInstance(): T
    abstract val qualifier: String?
    abstract val type: KType

    class Singleton<T : Any>(
        override val qualifier: String? = null,
        private val initializeMethod: () -> T,
    ) : LifeCycleType<T>() {
        private lateinit var instance: T
        override val type: KType =
            getLambdaReturnType(initializeMethod) ?: throw IllegalStateException(KTYPE_NULL_ERROR)

        override fun getInstance(): T {
            if (!::instance.isInitialized) instance = initializeMethod()
            return instance
        }
    }

//    class Disposable<T : Any> : LifeCycleType<T>
//
//    class Activity<T : Any> : LifeCycleType<T>
//
//    class Fragment<T : Any> : LifeCycleType<T>

    companion object {
        private const val KTYPE_NULL_ERROR = "인스턴스의 타입값이 Null 로 입력되었습니다."
    }
}
