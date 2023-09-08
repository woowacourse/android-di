package woowacourse.shopping.di.constructordi

import woowacourse.shopping.di.DefaultFakeRepository
import woowacourse.shopping.di.DefaultFirstDataSource
import woowacourse.shopping.di.DefaultSecondDataSource
import woowacourse.shopping.di.FakeRepository
import woowacourse.shopping.di.FirstDataSource
import woowacourse.shopping.di.Module
import woowacourse.shopping.di.SecondDataSource

object FakeConstructorDIModule : Module {
    fun provideFakeRepository(
        firstDataSource: FirstDataSource,
        secondDataSource: SecondDataSource,
    ): FakeRepository =
        DefaultFakeRepository(firstDataSource, secondDataSource)

    fun provideSecondDataSource(): SecondDataSource = DefaultSecondDataSource()
    fun provideFirstDataSource(): FirstDataSource = DefaultFirstDataSource()
}
