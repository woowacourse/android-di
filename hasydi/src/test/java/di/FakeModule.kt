package di

import woowacourse.shopping.hashdi.Module
import woowacourse.shopping.hashdi.annotation.Inject
import woowacourse.shopping.hashdi.annotation.Qualifier

object FakeModule : Module {

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

@Qualifier
annotation class RoomDB

@Qualifier
annotation class InMemory
