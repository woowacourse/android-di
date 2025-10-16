package com.yrsel.di.fixture.module

import com.yrsel.di.Module
import com.yrsel.di.annotation.Provides
import com.yrsel.di.fixture.FakeDataSource
import com.yrsel.di.fixture.FakeInMemoryDataSource
import com.yrsel.di.fixture.FakeRepository
import com.yrsel.di.fixture.FakeRepositoryConstructorFixture
import com.yrsel.di.fixture.InMemory

class FakeInMemoryFixtureModule : Module {
    @Provides
    @InMemory
    fun provideCartRepository(
        @InMemory dataSource: FakeDataSource,
    ): FakeRepository = FakeRepositoryConstructorFixture(dataSource)

    @Provides
    @InMemory
    fun provideDataSource(): FakeDataSource = FakeInMemoryDataSource()
}
