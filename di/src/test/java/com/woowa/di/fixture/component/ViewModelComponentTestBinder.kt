package com.woowa.di.fixture.component

import com.woowa.di.component.InstallIn
import com.woowa.di.viewmodel.ViewModelComponent

@InstallIn(ViewModelComponent::class)
object ViewModelComponentTestBinder {
    fun provideViewModelComponent(): TestViewModelComponent = FakeViewModelComponentImpl()
}
