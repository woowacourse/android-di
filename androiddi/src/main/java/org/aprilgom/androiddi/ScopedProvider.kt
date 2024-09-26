package org.aprilgom.androiddi

class ScopedProvider<T>(
    val scopeId: String,
    private val block: () -> T,
) : Provider<T> {
    private var instance: T? = null
    private var dropped: Boolean = false

    override fun get(): T {
        if (instance == null && !dropped) {
            instance = block()
        }
        return instance!!
    }

    fun drop() {
        instance = null
        dropped = true
    }
}
