package com.example.di

interface Module {
    fun provideInstance(dependencyRegistry: DiContainer)
}
