package com.example.di

interface TestDao

interface TestApi

@InstanceProvideModule
object DatabaseModule {
    @Provides
    fun provideTestDataBase(): TestDataBase = TestDataBase

    @Provides
    fun provideTestDao(
        @Inject dataBase: TestDataBase,
    ): TestDao = dataBase.testDao
}

@InstanceProvideModule
object RetrofitModule {
    @Provides
    fun provideTestApi(): TestApi = TestApiImpl()
}

object TestDataBase {
    val testDao = TestDaoImpl()
}

class TestDaoImpl : TestDao

class TestApiImpl : TestApi
