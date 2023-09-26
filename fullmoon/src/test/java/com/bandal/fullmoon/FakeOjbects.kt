package com.bandal.fullmoon

import javax.inject.Singleton

object FakeModule : Module {

    @Singleton
    @Qualifier("database")
    fun createDatabaseInstance(
        @Qualifier("localDataSource")
        localDataSource: FakeDataSource,
    ): FakeRepository {
        return FakeDatabaseRepository(localDataSource)
    }

    @Qualifier("inMemory")
    fun createInMemoryInstance(): FakeRepository {
        return FakeInMemoryRepository()
    }

    @Qualifier("localDataSource")
    fun createLocalDataSource(
        @FullMoonInject
        fakeDao: FakeDao,
    ): FakeDataSource {
        return FakeLocalDataSource(fakeDao)
    }

    fun createFakeDao(): FakeDao {
        return FakeDao()
    }

    fun createDateFormatter(): DateFormatter {
        return DateFormatter()
    }
}

interface FakeRepository

class FakeDatabaseRepository(
    @Qualifier("localDataSource")
    val fakeLocalDataSource: FakeDataSource,
) : FakeRepository

class FakeInMemoryRepository : FakeRepository

interface FakeDataSource

class FakeLocalDataSource(
    val fakeDao: FakeDao,
) : FakeDataSource

class FakeDao

class DateFormatter

class FakeClass(
    @Qualifier("database") val fakeDataBaseRepository: FakeRepository,
    @Qualifier("inMemory") val fakeInMemoryRepository: FakeRepository,
) {
    @FullMoonInject
    lateinit var fakeDateFormatter: DateFormatter

    @Qualifier("database")
    lateinit var fakeDataBaseRepository2: FakeRepository
}
