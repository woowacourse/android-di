package com.yrsel.di.fixture

import com.yrsel.di.annotation.Qualifier

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Qualifier
annotation class InMemory

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Qualifier
annotation class Room
