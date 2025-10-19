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
    @Volatile
    private var instance: T? = null

    override fun get(): T =
        instance ?: synchronized(this) {
            instance ?: creator().also { instance = it }
        }
}

class FactoryProvider<T>(
    private val creator: () -> T,
) : Provider<T> {
    override fun get(): T = creator()
}
