package com.woowa.di.fixture.qualifier

import com.woowa.di.component.InstallIn
import com.woowa.di.singleton.SingletonComponent
import com.woowa.di.singleton.SingletonComponent2

@InstallIn(SingletonComponent2::class)
class QualifierTestBinder {
    @FakeQualifier
    fun provideQualifierFake(): TestQualifier = FakeImpl()

    @Fake2Qualifier
    fun provideQualifierFake2(): TestQualifier = Fake2Impl()
}
