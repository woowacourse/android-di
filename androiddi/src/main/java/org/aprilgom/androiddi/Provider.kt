package org.aprilgom.androiddi

interface Provider<T> {
    fun get(): T
}
