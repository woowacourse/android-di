package com.woowa.di.fixture.component

import com.woowa.di.component.InstallIn
import com.woowa.di.singleton.SingletonComponent
import com.woowa.di.singleton.SingletonComponent2

@InstallIn(SingletonComponent2::class)
class SingletonComponentTestBinder {
    fun provideSingletonComponent(): TestSingletonComponent = FakeSingleComponentImpl()
}
