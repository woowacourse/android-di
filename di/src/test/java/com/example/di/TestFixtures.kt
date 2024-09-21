package com.example.di

import javax.inject.Singleton

@Singleton
interface TestDao

class TestDaoImpl : TestDao

interface Repository

class ARepository : Repository

class BRepository : Repository

class TestRepository2(
    @Inject
    private val dao: TestDao,
)

class TestUseCase(
    @Singleton
    @Inject
    private val repository2: TestRepository2,
    @Inject
    @Qualifier(ARepository::class)
    private val repository1: Repository,
)

@InstanceProvideModule
object DatabaseModule {
    @Provides
    fun provideTestDao(): TestDao = TestDaoImpl()
}

interface TestDataSource

@Singleton
class SingletonTestRepository(
    @Inject
    @Qualifier(ARepository::class)
    private val repository1: Repository,
)
