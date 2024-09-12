package woowacourse.shopping.data.di

import com.woowacourse.di.Module
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl

@Module
class RepositoryModule {
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()

    fun provideCartRepository(dao: CartProductDao): CartRepository = CartRepositoryImpl(dao = dao)
}
