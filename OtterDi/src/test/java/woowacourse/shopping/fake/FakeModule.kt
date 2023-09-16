package woowacourse.shopping.fake

import woowacourse.shopping.otterdi.Module
import woowacourse.shopping.otterdi.annotation.Qualifier

object FakeModule : Module {

    fun provideProductRepository(): FakeProductRepository = FakeDefaultProductRepository()

    @Qualifier("MultiImplCartRepositoryDefault")
    fun provideCartRepository(): FakeCartRepository = FakeDefaultCartRepository()

    @Qualifier("MultiImplCartRepositorySecond")
    fun provideCartRepository2(): FakeCartRepository = FakeDefaultCartRepository2()
}

interface FakeProductRepository
interface FakeCartRepository

class FakeDefaultProductRepository : FakeProductRepository
class FakeDefaultCartRepository : FakeCartRepository
class FakeDefaultCartRepository2 : FakeCartRepository
