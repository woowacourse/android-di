package com.woowa.di.fixture.component

import com.woowa.di.activity.ActivityRetainedComponent
import com.woowa.di.component.InstallIn

@InstallIn(ActivityRetainedComponent::class)
object ActivityComponentTestBinder {
    fun provideActivityComponent(): TestActivityComponent = FakeActivityComponentImpl()
}
