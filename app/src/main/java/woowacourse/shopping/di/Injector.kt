package woowacourse.shopping.di

interface Injector {
    fun <T : Any> inject(): T
}
