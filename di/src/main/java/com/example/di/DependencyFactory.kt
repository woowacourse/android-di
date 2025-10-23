package com.example.di

object DependencyFactory {
    fun <T : Any> create(modelClass: Class<T>): T {
        val instance = modelClass.getDeclaredConstructor().newInstance()
        DependencyInjection.inject(instance)
        return instance
    }
}