package di

import woowacourse.shopping.hashdi.Module
import woowacourse.shopping.hashdi.annotation.Inject
import woowacourse.shopping.hashdi.annotation.Qualifier

object FakeModule : Module {

    fun provideProductRepository(): ProductFakeRepository = ProductFakeRepository

    fun provideCartRepository(): FakeCartRepository = FakeCartDefaultRepository

    @InMemory
    fun provideInMemoryCartRepository(): FakeCartRepository = FakeCartInMemoryRepository

    fun provideRepositoryWithDataSource(
        @Inject
        @RoomDB
        fakeDataSource: FakeDataSource,
    ) = FakeRepositoryWithDataSource(fakeDataSource)

    @RoomDB
    fun provideRoomDataSource(): FakeDataSource = FakeRoomDataSource

    @InMemory
    fun provideInMemoryDataSource(): FakeDataSource = FakeInMemoryDataSource
}

object ProductFakeRepository

interface FakeCartRepository

object FakeCartDefaultRepository : FakeCartRepository

object FakeCartInMemoryRepository : FakeCartRepository

class FakeRepositoryWithDataSource(
    @Inject
    @RoomDB
    val fakeDatasource: FakeDataSource,
)

interface FakeDataSource

@InMemory
object FakeInMemoryDataSource : FakeDataSource

@RoomDB
object FakeRoomDataSource : FakeDataSource

@Qualifier
annotation class RoomDB

@Qualifier
annotation class InMemory
