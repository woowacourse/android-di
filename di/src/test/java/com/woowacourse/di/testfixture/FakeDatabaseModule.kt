package com.woowacourse.di.testfixture

import android.content.Context
import com.woowacourse.di.ApplicationContext
import com.woowacourse.di.Module

@Module
class FakeDatabaseModule {
    fun provideShoppingDatabase(
        @ApplicationContext context: Context,
    ): FakeRoomDatabase = FakeShoppingDatabase(context = context)
}
