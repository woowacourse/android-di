package woowacourse.shopping.di

object FakeRepositoryModule : Module {
    fun provideProductRepository(): ProductFakeRepository = ProductFakeRepository
    fun provideCartRepository(): CartFakeRepository = CartFakeRepository
}

object ProductFakeRepository

object CartFakeRepository
