package com.example.di

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier

@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
annotation class Remote

@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
annotation class Local

@Target(AnnotationTarget.FIELD)
annotation class Inject

@Target(AnnotationTarget.CLASS)
annotation class Singleton

@Target(AnnotationTarget.CLASS)
annotation class ViewModelScope

@Target(AnnotationTarget.CLASS)
annotation class ActivityScope