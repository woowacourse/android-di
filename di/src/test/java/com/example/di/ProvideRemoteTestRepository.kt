package com.example.di

@Qualifier(RemoteTestRepository::class)
@Target(AnnotationTarget.PROPERTY)
annotation class ProvideRemoteTestRepository

@Qualifier(LocalTestRepository::class)
@Target(AnnotationTarget.PROPERTY)
annotation class ProvideLocalTestRepository

@Qualifier(RemoteTestDataSource::class)
@Target(AnnotationTarget.PROPERTY)
annotation class ProvideRemoteTestDataSource

@Qualifier(LocalTestDataSource::class)
@Target(AnnotationTarget.PROPERTY)
annotation class ProvideLocalTestDataSource
