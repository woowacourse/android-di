package com.example.alsonglibrary2.fixtures

import com.example.alsonglibrary2.di.AlsongQualifier
import com.example.alsonglibrary2.di.LibraryDependencyProvider
import com.example.alsonglibrary2.fixtures.instance.defaultFakeRepository1
import com.example.alsonglibrary2.fixtures.instance.defaultFakeRepository2
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

@AlsongQualifier
annotation class DefaultRepository0

@AlsongQualifier
annotation class DefaultRepository1

@AlsongQualifier
annotation class DefaultRepository2

object QualifiedDependencyProvider : LibraryDependencyProvider {
    @DefaultRepository1
    fun provideFakeCartRepository1(): FakeRepository {
        return defaultFakeRepository1
    }

    @DefaultRepository2
    fun provideFakeCartRepository2(): FakeRepository {
        return defaultFakeRepository2
    }
}
