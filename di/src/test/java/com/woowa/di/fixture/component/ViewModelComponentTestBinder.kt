package com.woowa.di.fixture.component

import com.woowa.di.component.InstallIn
import com.woowa.di.viewmodel.ViewModelComponent

@InstallIn(ViewModelComponent::class)
class ViewModelComponentTestBinder {
    fun provideViewModelComponent(): TestViewModelComponent = FakeViewModelComponentImpl()
}
