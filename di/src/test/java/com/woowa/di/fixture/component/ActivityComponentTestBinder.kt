package com.woowa.di.fixture.component

import com.woowa.di.activity.ActivityComponent
import com.woowa.di.component.InstallIn

@InstallIn(ActivityComponent::class)
class ActivityComponentTestBinder{
    fun provideActivityComponent(): TestActivityComponent = FakeActivityComponentImpl()
}
