package com.example.di

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class Inject

@Target(AnnotationTarget.PROPERTY)
annotation class Dependency

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Lifespan

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Lifespan
annotation class ApplicationLifespan

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Lifespan
annotation class ViewModelLifespan

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Lifespan
annotation class ActivityLifespan
