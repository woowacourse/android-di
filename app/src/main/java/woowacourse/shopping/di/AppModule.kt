package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartDefaultRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ProductDefaultRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class AppModule(private val context: Context) : Module {

    fun provideProductRepository(): ProductRepository = ProductDefaultRepository()

    fun provideCartRepository(
        @Inject cartProductDao: CartProductDao,
    ): CartRepository = CartDefaultRepository(cartProductDao)

    fun provideCartProductDao(): CartProductDao =
        ShoppingDatabase.getInstance(context).cartProductDao()
}
