package woowacourse.di

import android.content.Context
import woowacourse.di.annotation.InjectField
import woowacourse.di.annotation.Singleton

class FakeModule : Module {

    override lateinit var context: Context

    override fun setModuleContext(context: Context) {
        this.context = context
    }

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
