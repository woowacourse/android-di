package woowacourse.shopping.util.autoDI

import kotlin.reflect.KType

sealed class LifeCycleType<T : Any> {
    abstract fun getInstance(): T
    abstract val qualifier: String?

    class Singleton<T : Any>(
        override val qualifier: String? = null,
        private val initializeMethod: () -> T,
    ) : LifeCycleType<T>() {
        private lateinit var instance: T
        private val type: KType = ::initializeMethod.returnType

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
}
