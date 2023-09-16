package woowacourse.di

import woowacourse.di.annotation.InjectField

class FakeModule : Module {

    fun provideFakeProductRepository(): FakeProductRepository = DefaultFakeProductRepository()

    fun provideInMemoryFakeCartRepository(): FakeCartRepository = InMemoryFakeCartRepository()

    fun provideInDiskCartRepository(): FakeCartRepository =
        InDiskFakeCartRepository(FakeRoomDataSource())

    fun provideRecursiveRepository(@InjectField dataSource: FakeDataSource) =
        InDiskFakeCartRepository(dataSource)

    fun provideFakeRemoteDataSource(): FakeDataSource = FakeRemoteDataSource()
}

interface FakeProductRepository

class DefaultFakeProductRepository : FakeProductRepository

interface FakeCartRepository

class InMemoryFakeCartRepository : FakeCartRepository

class InDiskFakeCartRepository(val dataSource: FakeDataSource) : FakeCartRepository

interface FakeDataSource
class FakeRoomDataSource : FakeDataSource
class FakeRemoteDataSource : FakeDataSource
