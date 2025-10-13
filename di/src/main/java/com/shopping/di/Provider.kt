package com.shopping.di

/**
 * @param T 반환할 객체 타입
 * @return 제공 객체
 */
fun interface Provider<T> {
    fun get(): T
}
