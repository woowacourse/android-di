package com.example.di

import android.content.Context
import com.example.di.annotation.Qualifier
import com.example.di.application.DiApplication
import com.example.di.module.ApplicationModule

class FakeApplication : DiApplication(FakeApplicationModule::class.java)

class FakeApplicationModule(context: Context) : ApplicationModule(context) {

    @FakeInMemoryCartRepository
    fun getInMemoryCartRepository(@FakeInMemory localDataSource: FakeLocalDataSource): FakeCartRepository {
        return getOrCreateInstance {
            FakeImMemoryCartRepository(FakeInMemoryLocalDataSource())
        }
    }

    @FakeRoomDbCartRepository
    fun getRoomCartRepository(@FakeRoomDb localDataSource: FakeLocalDataSource): FakeCartRepository {
        return getOrCreateInstance {
            FakeDefaultCartRepository(localDataSource)
        }
    }

    @FakeInMemory
    fun getInMemoryLocalDataSource(): FakeLocalDataSource {
        return getOrCreateInstance { FakeInMemoryLocalDataSource() }
    }

    @FakeRoomDb
    fun getDefaultLocalDataSource(): FakeLocalDataSource {
        return getOrCreateInstance { FakeDefaultLocalDataSource() }
    }
}

interface FakeCartRepository
class FakeDefaultCartRepository(private val localDataSource: FakeLocalDataSource) :
    FakeCartRepository

class FakeImMemoryCartRepository(private val localDataSource: FakeLocalDataSource) :
    FakeCartRepository

interface FakeLocalDataSource
class FakeDefaultLocalDataSource : FakeLocalDataSource
class FakeInMemoryLocalDataSource : FakeLocalDataSource

interface FakeProductRepository
class FakeDefaultProductRepository : FakeProductRepository

@Qualifier
annotation class FakeRoomDb

@Qualifier
annotation class FakeInMemory

@Qualifier
annotation class FakeRoomDbCartRepository

@Qualifier
annotation class FakeInMemoryCartRepository
