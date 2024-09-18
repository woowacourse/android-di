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

@Qualifier
annotation class FakeRepository6

@Qualifier
annotation class FakeRepository7

@Qualifier
annotation class FakeRepository8

@Qualifier
annotation class FakeDataSource1

@Qualifier
annotation class FakeDataSource2
