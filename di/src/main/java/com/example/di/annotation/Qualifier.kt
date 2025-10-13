package com.example.di.annotation

@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier

@Qualifier
annotation class InMemory

@Qualifier
annotation class Database
