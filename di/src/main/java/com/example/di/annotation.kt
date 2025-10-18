package com.example.di

@Target(AnnotationTarget.FIELD)
annotation class Inject

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier

@Qualifier
annotation class Local

@Qualifier
annotation class Remote

@Qualifier
annotation class InMemory

@Qualifier
annotation class RoomDB

@Target(AnnotationTarget.CLASS)
annotation class Singleton

@Target(AnnotationTarget.CLASS)
annotation class ViewModelScope

@Target(AnnotationTarget.CLASS)
annotation class ActivityScope