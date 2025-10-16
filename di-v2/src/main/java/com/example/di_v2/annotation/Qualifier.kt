package com.example.di_v2.annotation

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier

@Qualifier
annotation class InMemory

@Qualifier
annotation class Database
