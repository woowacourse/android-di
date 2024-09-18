
package com.kmlibs.supplin.fixtures.android.module

import com.kmlibs.supplin.annotations.Module
import com.kmlibs.supplin.fixtures.FakeRepository1
import com.kmlibs.supplin.fixtures.FakeRepository2
import com.kmlibs.supplin.fixtures.android.repository.DefaultFakeRepository1
import com.kmlibs.supplin.fixtures.android.repository.DefaultFakeRepository2
import com.kmlibs.supplin.fixtures.android.repository.FakeRepository

@Module
object FakeModule {
    @FakeRepository1
    fun provideFakeRepository1(): FakeRepository = DefaultFakeRepository1()

    @FakeRepository2
    fun provideFakeRepository2(): FakeRepository = DefaultFakeRepository2()
}
