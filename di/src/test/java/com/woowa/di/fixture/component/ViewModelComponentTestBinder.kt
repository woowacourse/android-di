package com.woowa.di.fixture.component

import com.woowa.di.component.InstallIn
import com.woowa.di.viewmodel.ViewModelComponent2

@InstallIn(ViewModelComponent2::class)
class ViewModelComponentTestBinder {
    fun provideViewModelComponent(): TestViewModelComponent = FakeViewModelComponentImpl()
}
