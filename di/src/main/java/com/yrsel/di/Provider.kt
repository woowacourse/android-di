package com.yrsel.di

/**
 * @param T 반환할 객체 타입
 * @return 제공 객체
 */
sealed interface Provider<T> {
    fun get(): T
}

class SingletonProvider<T>(
    private val creator: () -> T,
) : Provider<T> {
    private var instance: T? = null

    override fun get(): T {
        if (instance == null) {
            synchronized(this) {
                if (instance == null) instance = creator()
            }
        }
        return instance!!
    }
}

class FactoryProvider<T>(
    private val creator: () -> T,
) : Provider<T> {
    override fun get(): T = creator()
}
