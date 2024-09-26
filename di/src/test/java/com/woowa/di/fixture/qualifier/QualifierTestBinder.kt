package com.woowa.di.fixture.qualifier

import com.woowa.di.component.InstallIn
import com.woowa.di.singleton.SingletonComponent

@InstallIn(SingletonComponent::class)
object QualifierTestBinder {
    @FakeQualifier
    fun provideQualifierFake(): TestQualifier = FakeImpl()

    @Fake2Qualifier
    fun provideQualifierFake2(): TestQualifier = Fake2Impl()
}
