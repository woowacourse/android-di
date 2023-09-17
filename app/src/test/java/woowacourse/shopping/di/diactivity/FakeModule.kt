package woowacourse.shopping.di.diactivity

import com.di.berdi.Module
import com.di.berdi.annotation.Qualifier
import woowacourse.shopping.di.DefaultFirstDataSource
import woowacourse.shopping.di.DefaultSecondDataSource
import woowacourse.shopping.di.FakeObj
import woowacourse.shopping.di.FakeRepository
import woowacourse.shopping.di.FirstDataSource
import woowacourse.shopping.di.InMemoryFakeRepository
import woowacourse.shopping.di.OnDiskFakeRepository
import woowacourse.shopping.di.SecondDataSource

object FakeModule : Module {
    fun provideFakeObj(): FakeObj = FakeObj()

    @Qualifier(qualifiedName = "OnDisk")
    fun provideOnDiskFakeRepository(
        firstDataSource: FirstDataSource,
        secondDataSource: SecondDataSource,
    ): FakeRepository = OnDiskFakeRepository(firstDataSource, secondDataSource)

    @Qualifier(qualifiedName = "InMemory")
    fun provideInMemoryFakeRepository(): FakeRepository = InMemoryFakeRepository()

    fun provideSecondDataSource(): SecondDataSource = DefaultSecondDataSource()
    fun provideFirstDataSource(): FirstDataSource = DefaultFirstDataSource()
}
