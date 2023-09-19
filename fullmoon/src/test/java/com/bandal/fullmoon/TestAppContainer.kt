package com.bandal.fullmoon

object TestAppContainer : AppContainer() {

    @SingleTone
    @Qualifier("database")
    fun createDatabaseInstance(
        @Qualifier("localDataSource")
        localDataSource: FakeDataSource,
    ): FakeImplementWithQualifierDatabase {
        return FakeImplementWithQualifierDatabase(localDataSource)
    }

    @Qualifier("inMemory")
    fun createInMemoryInstance(): FakeImplementWithQualifierInMemory {
        return FakeImplementWithQualifierInMemory()
    }

    @Qualifier("localDataSource")
    fun createLocalDataSource(): FakeLocalDataSource {
        return FakeLocalDataSource()
    }

    fun createDateFormatter(): DateFormatter {
        return DateFormatter()
    }
}

interface FakeRepository
interface FakeDataSource
class DateFormatter

class FakeImplementWithQualifierDatabase(
    @Qualifier("localDataSource") val fakeLocalDataSource: FakeDataSource,
) : FakeRepository

class FakeImplementWithQualifierInMemory : FakeRepository

class FakeLocalDataSource : FakeDataSource

class FakeClass(
    @Qualifier("database") val fakeDataBaseRepository: FakeRepository,
    @Qualifier("inMemory") val fakeInMemoryRepository: FakeRepository,
) {
    @FullMoonInject
    lateinit var fakeDateFormatter: DateFormatter

    @Qualifier("database")
    lateinit var fakeDataBaseRepository2: FakeRepository
}
