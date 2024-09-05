package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class AppDiModule : DIModule {
    override fun singletonInstance(): List<Module> {
        return listOf(
            Module(CartRepository::class, DefaultCartRepository()),
            Module(ProductRepository::class, DefaultProductRepository()),
        )
    }
}
