package com.bandal.di

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CONSTRUCTOR)
annotation class BandalInject

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier

@Qualifier
annotation class Database

@Qualifier
annotation class InMemory
