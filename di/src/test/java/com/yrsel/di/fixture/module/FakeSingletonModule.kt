package com.yrsel.di.fixture.module

import com.yrsel.di.Module
import com.yrsel.di.annotation.Provides
import com.yrsel.di.annotation.SingletonScope
import com.yrsel.di.fixture.FakeDataSource
import com.yrsel.di.fixture.FakeInMemoryDataSource
import com.yrsel.di.fixture.FakeRepository
import com.yrsel.di.fixture.FakeRepositoryConstructorInject

class FakeSingletonModule : Module {
    @Provides
    @SingletonScope
    fun provideCartRepository(dataSource: FakeDataSource): FakeRepository = FakeRepositoryConstructorInject(dataSource)

    @Provides
    fun provideDataSource(): FakeDataSource = FakeInMemoryDataSource()
}
