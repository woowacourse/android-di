package woowacourse.di

import woowacourse.di.annotation.InjectField
import woowacourse.di.annotation.Singleton
import woowacourse.di.module.Module

class FakeModule : Module {

    fun provideFakeProductRepository(): FakeProductRepository = DefaultFakeProductRepository()

    fun provideInMemoryFakeCartRepository(): FakeCartRepository = InMemoryFakeCartRepository()

    fun provideInDiskCartRepository(): FakeCartRepository =
        InDiskFakeCartRepository(FakeRoomDataSource())

    fun provideRecursiveRepository(@InjectField dataSource: FakeDataSource) =
        InDiskFakeCartRepository(dataSource)

    fun provideFakeRemoteDataSource(): FakeDataSource = FakeRemoteDataSource()

    @Singleton
    fun provideSingletonRepository(): FakeSingleRepository = SingleRepository()
}

interface FakeProductRepository

class DefaultFakeProductRepository : FakeProductRepository

interface FakeCartRepository

class InMemoryFakeCartRepository : FakeCartRepository

class InDiskFakeCartRepository(val dataSource: FakeDataSource) : FakeCartRepository

interface FakeDataSource
class FakeRoomDataSource : FakeDataSource
class FakeRemoteDataSource : FakeDataSource
interface FakeSingleRepository
class SingleRepository : FakeSingleRepository
