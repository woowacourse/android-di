package com.example.alsonglibrary2.fixtures

import com.example.alsonglibrary2.di.QualifierDependencyProvider
import com.example.alsonglibrary2.di.anotations.AlsongQualifier
import com.example.alsonglibrary2.fixtures.instance.defaultFakeRepository1
import com.example.alsonglibrary2.fixtures.instance.defaultFakeRepository2
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

@AlsongQualifier
annotation class DefaultFakeRepository0

@AlsongQualifier
annotation class DefaultFakeRepository1

@AlsongQualifier
annotation class DefaultFakeRepository2

object FakeQualifierDependencyProvider : QualifierDependencyProvider {
    @DefaultFakeRepository1
    fun provideFakeCartRepository1(): FakeRepository {
        return defaultFakeRepository1
    }

    @DefaultFakeRepository2
    fun provideFakeCartRepository2(): FakeRepository {
        return defaultFakeRepository2
    }
}
