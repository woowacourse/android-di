package com.woowa.di.fixture.component

import com.woowa.di.component.InstallIn
import com.woowa.di.singleton.SingletonComponent

@InstallIn(SingletonComponent::class)
object SingletonComponentTestBinder {
    fun provideSingletonComponent(): TestSingletonComponent = FakeSingleComponentImpl()
}
