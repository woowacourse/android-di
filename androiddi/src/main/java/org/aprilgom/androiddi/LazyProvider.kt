package org.aprilgom.androiddi


class LazyProvider<T>(
    private val block: () -> T) : Provider<T> {
    private var instance: T? = null
    override fun get(): T {
        if (instance == null) {
            instance = block()
        }
        return instance!!
    }
}
