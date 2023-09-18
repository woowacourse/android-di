package woowacourse.shopping.hasydi

import woowacourse.shopping.hasydi.annotation.Inject
import woowacourse.shopping.hasydi.annotation.Qualifier
import woowacourse.shopping.hasydi.annotation.Singleton

object FakeModule : Module {

    fun provideProductRepository(): ProductFakeRepository = ProductFakeRepository

    fun provideCartRepository(): FakeCartRepository = FakeCartDefaultRepository

    @InMemory
    fun provideInMemoryCartRepository(): FakeCartRepository = FakeCartInMemoryRepository

    @Singleton
    fun provideRepositoryWithDataSource(
        @Inject
        @RoomDB
        fakeDataSource: FakeDataSource,
    ) = FakeRepositoryWithDataSource(fakeDataSource)

    @RoomDB
    fun provideRoomDataSource(): FakeDataSource = FakeRoomDataSource

    @InMemory
    fun provideInMemoryDataSource(): FakeDataSource = FakeInMemoryDataSource

    @Singleton
    fun provideSingletonRepository(): FakeSingletonRepository = FakeSingletonRepository()

    fun provideDisposableRepository(): FakeDisposableRepository = FakeDisposableRepository()
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

class FakeSingletonRepository()

class FakeDisposableRepository()

interface FakeDataSource

@InMemory
object FakeInMemoryDataSource : FakeDataSource

@RoomDB
object FakeRoomDataSource : FakeDataSource

@Qualifier
annotation class RoomDB

@Qualifier
annotation class InMemory
