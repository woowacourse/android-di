package org.aprilgom.androiddi

class Scope(
    val name: String,
) {
    inline fun <reified T> get(name: String): T = GlobalContext.provide(name, T::class) as T

    fun close() {
        GlobalContext.deleteScopeInstance(name)
    }
}
