package com.kmlibs.supplin.fixtures

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FooQualifier1

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FooQualifier2

@Qualifier
annotation class FakeRepository1

@Qualifier
annotation class FakeRepository2
