package com.example.di.model

internal class SingletonProvider<T : Any>(
    private val factory: (Map<BindingKey, Any>) -> T,
) : Provider<T> {
    @Volatile
    private var cached: T? = null

    override fun get(overrides: Map<BindingKey, Any>): T {
        cached?.let { return it }
        return synchronized(this) {
            cached ?: factory(overrides).also { cached = it }
        }
    }
}
