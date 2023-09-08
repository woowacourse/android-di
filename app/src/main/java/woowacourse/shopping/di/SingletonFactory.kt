package woowacourse.shopping.di

interface SingletonFactory<T : Any> {

    fun getInstance(): T
}
