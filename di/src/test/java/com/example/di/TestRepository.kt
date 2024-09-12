package com.example.di

interface TestRepository

class RemoteTestRepository : TestRepository {
    @Inject
    @ProvideRemoteTestDataSource
    var dataSource: TestDataSource? = null
}

class LocalTestRepository(
    @Inject
    @ProvideLocalTestDataSource
    val dataSource: TestDataSource,
) : TestRepository

class ConstructorInjectedRepository(
    @Inject
    @ProvideRemoteTestDataSource
    val dataSource: TestDataSource,
) : TestRepository

class FieldInjectedRepository : TestRepository {
    @Inject
    @ProvideRemoteTestDataSource
    var dataSource: TestDataSource? = null
}
