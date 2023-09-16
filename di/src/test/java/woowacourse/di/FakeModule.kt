package woowacourse.di

class FakeModule : Module {

    fun provideFakeProductRepository(): FakeProductRepository = DefaultFakeProductRepository()

    fun provideInMemoryFakeCartRepository(): FakeCartRepository = InMemoryFakeCartRepository()

    fun provideInDiskCartRepository(): FakeCartRepository =
        InDiskFakeCartRepository(FakeRoomDataSource())
}

interface FakeProductRepository

class DefaultFakeProductRepository : FakeProductRepository

interface FakeCartRepository

class InMemoryFakeCartRepository : FakeCartRepository

class InDiskFakeCartRepository(val dataSource: FakeDataSource) : FakeCartRepository

interface FakeDataSource
class FakeRoomDataSource : FakeDataSource
