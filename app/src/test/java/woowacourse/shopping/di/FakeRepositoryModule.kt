package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.RoomDB

object FakeRepositoryModule : Module {

    fun provideProductRepository(): ProductFakeRepository = ProductFakeRepository

    fun provideCartRepository(): CartFakeRepository = CartFakeRepository

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

object CartFakeRepository

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
