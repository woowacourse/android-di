package com.di.berdi.fake

class OnDiskFakeRepository(
    val firstDataSource: FirstDataSource,
    val secondDataSource: SecondDataSource,
) : FakeRepository
