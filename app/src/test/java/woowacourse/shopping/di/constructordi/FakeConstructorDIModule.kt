package woowacourse.shopping.di.constructordi

import woowacourse.shopping.di.DefaultFakeRepository
import woowacourse.shopping.di.FakeRepository
import woowacourse.shopping.di.Module

object FakeConstructorDIModule : Module {
    fun provideFakeRepository(): FakeRepository = DefaultFakeRepository()
}
