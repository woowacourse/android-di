package com.example.di

@Target(AnnotationTarget.PROPERTY)
annotation class Inject

@Target(AnnotationTarget.FUNCTION)
annotation class Dependency

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Lifespan

@Target(AnnotationTarget.FUNCTION)
@Lifespan
annotation class ApplicationLifespan

@Target(AnnotationTarget.FUNCTION)
@Lifespan
annotation class ViewModelLifespan

@Target(AnnotationTarget.FUNCTION)
@Lifespan
annotation class ActivityLifespan
