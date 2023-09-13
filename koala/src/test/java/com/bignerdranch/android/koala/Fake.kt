package com.bignerdranch.android.koala

class FakeContainer : Container {

    fun getProductRepository(): FakeProductRepository {
        return FakeDefaultProductRepository()
    }

    @FakeInMemoryRepository
    fun getInMemoryCartRepository(
        @FakeInMemoryDataSource fakeDataSource: FakeDataSource,
    ): FakeCartRepository {
        return FakeImMemoryCartRepository(fakeDataSource)
    }

    @FakeRoomDBRepository
    fun getRoomDBCartRepository(
        @FakeRoomDBDataSource fakeDataSource: FakeDataSource,
    ): FakeCartRepository {
        return FakeDefaultCartRepository(fakeDataSource)
    }

    @FakeInMemoryDataSource
    fun getInMemoryCartDataSource(): FakeDataSource {
        return FakeMemoryDataSource()
    }

    @FakeRoomDBDataSource
    fun getRoomDBCartDataSource(): FakeDataSource {
        return FakeDefaultDataSource()
    }
}

interface FakeCartRepository
class FakeDefaultCartRepository(private val localDataSource: FakeDataSource) : FakeCartRepository
class FakeImMemoryCartRepository(private val localDataSource: FakeDataSource) : FakeCartRepository

interface FakeDataSource
class FakeDefaultDataSource : FakeDataSource
class FakeMemoryDataSource : FakeDataSource

interface FakeProductRepository
class FakeDefaultProductRepository : FakeProductRepository

@KoalaQualifier
annotation class FakeRoomDBRepository

@KoalaQualifier
annotation class FakeInMemoryRepository

@KoalaQualifier
annotation class FakeRoomDBDataSource

@KoalaQualifier
annotation class FakeInMemoryDataSource
