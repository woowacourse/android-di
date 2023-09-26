package com.di.berdi.fake

import com.di.berdi.Module
import com.di.berdi.annotation.Qualifier

object FakeModule : Module {
    fun provideFakeObj(): FakeObj = FakeObj

    @Qualifier(qualifiedName = "OnDisk")
    fun provideOnDiskFakeRepository(
        firstDataSource: FirstDataSource,
        secondDataSource: SecondDataSource,
    ): FakeRepository = OnDiskFakeRepository(firstDataSource, secondDataSource)

    @Qualifier(qualifiedName = "InMemory")
    fun provideInMemoryFakeRepository(): FakeRepository = InMemoryFakeRepository()

    fun provideSecondDataSource(): SecondDataSource = DefaultSecondDataSource()
    fun provideFirstDataSource(): FirstDataSource = DefaultFirstDataSource()
}
