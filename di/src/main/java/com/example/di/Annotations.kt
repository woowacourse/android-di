package com.example.di

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class Inject

@Target(AnnotationTarget.PROPERTY)
annotation class Dependency

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier
