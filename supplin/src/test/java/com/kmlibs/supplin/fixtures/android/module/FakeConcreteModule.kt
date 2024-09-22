
package com.kmlibs.supplin.fixtures.android.module

import com.kmlibs.supplin.annotations.Abstract
import com.kmlibs.supplin.annotations.Concrete
import com.kmlibs.supplin.annotations.Module
import com.kmlibs.supplin.fixtures.android.FakeRepository1
import com.kmlibs.supplin.fixtures.android.FakeRepository2
import com.kmlibs.supplin.fixtures.android.datasource.DefaultFakeDataSource1
import com.kmlibs.supplin.fixtures.android.datasource.DefaultFakeDataSource2
import com.kmlibs.supplin.fixtures.android.datasource.FakeDataSource
import com.kmlibs.supplin.fixtures.android.repository.DefaultFakeRepository1
import com.kmlibs.supplin.fixtures.android.repository.DefaultFakeRepository2
import com.kmlibs.supplin.fixtures.android.repository.DefaultFakeRepository6
import com.kmlibs.supplin.fixtures.android.repository.DefaultFakeRepository7
import com.kmlibs.supplin.fixtures.android.repository.DefaultFakeRepository8
import com.kmlibs.supplin.fixtures.android.repository.FakeRepository

@Module
object FakeConcreteModule {
    @Concrete
    @FakeRepository1
    fun provideFakeRepository1(): FakeRepository = DefaultFakeRepository1()

    @Concrete
    @FakeRepository2
    fun provideFakeRepository2(): FakeRepository = DefaultFakeRepository2()
}

@Module
interface FakeRepositoryModule {
    @Abstract
    fun bindFakeRepository6(impl: DefaultFakeRepository6): FakeRepository

    @Abstract
    fun bindFakeRepository7(impl: DefaultFakeRepository7): FakeRepository

    @Abstract
    fun bindFakeRepository8(impl: DefaultFakeRepository8): FakeRepository
}

@Module
interface FakeDataSourceModule {
    @Abstract
    fun bindFakeDataSource1(impl: DefaultFakeDataSource1): FakeDataSource

    @Abstract
    fun bindFakeDataSource2(impl: DefaultFakeDataSource2): FakeDataSource
}
