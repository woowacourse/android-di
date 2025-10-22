package com.yrsel.di

data class ScopedProvider<T>(
    val provider: Provider<T>,
    val scope: ScopeType,
) : Provider<T> {
    override fun get(): T =
        when (provider) {
            is SingletonProvider -> provider.get()
            else -> (provider as FactoryProvider).get()
        }
}
