package woowacourse.shopping.di.constructordi

import woowacourse.shopping.di.DefaultFirstDataSource
import woowacourse.shopping.di.DefaultSecondDataSource
import woowacourse.shopping.di.FakeRepository
import woowacourse.shopping.di.FirstDataSource
import woowacourse.shopping.di.InMemoryFakeRepository
import woowacourse.shopping.di.Module
import woowacourse.shopping.di.OnDiskFakeRepository
import woowacourse.shopping.di.SecondDataSource
import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.di.annotation.OnDisk

object FakeConstructorDIModule : Module {

    @OnDisk
    fun provideOnDiskFakeRepository(
        firstDataSource: FirstDataSource,
        secondDataSource: SecondDataSource,
    ): FakeRepository = OnDiskFakeRepository(firstDataSource, secondDataSource)

    @InMemory
    fun provideInMemoryFakeRepository(): FakeRepository = InMemoryFakeRepository()

    fun provideSecondDataSource(): SecondDataSource = DefaultSecondDataSource()
    fun provideFirstDataSource(): FirstDataSource = DefaultFirstDataSource()
}
