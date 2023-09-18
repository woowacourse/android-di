package com.example.di.annotation

@Target(AnnotationTarget.PROPERTY)
annotation class FieldInject

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier
