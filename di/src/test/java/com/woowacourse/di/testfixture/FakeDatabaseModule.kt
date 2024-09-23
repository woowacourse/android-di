package com.woowacourse.di.testfixture

import android.content.Context
import com.woowacourse.di.ApplicationContext
import com.woowacourse.di.DiModule

@DiModule
class FakeDatabaseModule {
    fun provideShoppingDatabase(
        @ApplicationContext context: Context,
    ): FakeRoomDatabase = FakeShoppingDatabase(context = context)
}
