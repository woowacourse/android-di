package com.example.alsonglibrary2.fixtures

import com.example.alsonglibrary2.di.AlsongQualifier
import com.example.alsonglibrary2.di.LibraryDependencyProvider
import com.example.alsonglibrary2.fixtures.instance.defaultFakeRepository
import com.example.alsonglibrary2.fixtures.repository.FakeRepository

@AlsongQualifier
annotation class DefaultRepository

object DependencyProvider : LibraryDependencyProvider {
    @DefaultRepository
    fun provideFakeCartRepository(): FakeRepository {
        return defaultFakeRepository
    }
}
