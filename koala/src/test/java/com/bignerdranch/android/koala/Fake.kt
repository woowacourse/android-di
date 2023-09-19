package com.bignerdranch.android.koala

import android.content.Context
import androidx.lifecycle.ViewModel

class FakeApplicationDiModule() : DiModule {

    override var context: Context? = null

    fun getFakeRepository(): FakeRepository {
        return FakeRepository()
    }
}

class FakeDiApplication : DiApplication(FakeApplicationDiModule())

class FakeViewModel(
    val repository: FakeRepository,
) : ViewModel()

class FakeRepository

class FakeActivityModule() : DiModule {

    override var context: Context? = null

    fun getFakeFieldObject(): FakeFieldObject {
        return FakeFieldObject()
    }
}

class FakeFieldObject

class FakeModule() : DiModule {

    override var context: Context? = null

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
    fun getRoomDBCartDataSource(dao: FakeDao): FakeDataSource {
        return FakeDefaultDataSource()
    }

    @KoalaSingleton
    fun getSingletonRepository(): FakeSingletonRepository {
        return FakeSingletonRepository()
    }

    fun getNonSingletonRepository(): FakeNonSingletonRepository {
        return FakeNonSingletonRepository()
    }

    fun getFakeDao(): FakeDao {
        return FakeDao()
    }
}

interface FakeCartRepository
class FakeDefaultCartRepository(private val localDataSource: FakeDataSource) : FakeCartRepository
class FakeImMemoryCartRepository(private val localDataSource: FakeDataSource) : FakeCartRepository

interface FakeDataSource
class FakeDefaultDataSource : FakeDataSource
class FakeMemoryDataSource : FakeDataSource

class FakeDao

interface FakeProductRepository
class FakeDefaultProductRepository : FakeProductRepository

class FakeSingletonRepository
class FakeNonSingletonRepository

@KoalaQualifier
annotation class FakeRoomDBRepository

@KoalaQualifier
annotation class FakeInMemoryRepository

@KoalaQualifier
annotation class FakeRoomDBDataSource

@KoalaQualifier
annotation class FakeInMemoryDataSource
