package woowacourse.shopping.di

class InstanceHolder<T> {
    private var value: T? = null

    fun get(block: () -> T): T {
        if (value == null) value = block()
        return value!!
    }
}
