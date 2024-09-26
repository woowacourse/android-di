package com.example.di

import com.example.di.annotation.Inject
import com.example.di.annotation.InstanceProvideModule
import com.example.di.annotation.Provides

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
