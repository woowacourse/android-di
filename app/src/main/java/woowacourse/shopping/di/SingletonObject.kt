package woowacourse.shopping.di

interface SingletonObject<T : Any> {

    fun get(): T
}
