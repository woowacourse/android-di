package com.yrsel.di.fixture.module

import com.yrsel.di.Module
import com.yrsel.di.annotation.Provides
import com.yrsel.di.fixture.FakeDataSource
import com.yrsel.di.fixture.FakeInMemoryDataSource
import com.yrsel.di.fixture.FakeRoomDataSource
import com.yrsel.di.fixture.InMemory
import com.yrsel.di.fixture.Room

class FakeQualifierModule : Module {
    @Provides
    @InMemory
    fun provideInMemoryDataSource(): FakeDataSource = FakeInMemoryDataSource()

    @Provides
    @Room
    fun provideRoomDataSource(): FakeDataSource = FakeRoomDataSource()
}
