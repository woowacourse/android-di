package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.cart.CartProductDao
import woowacourse.shopping.data.cart.DefaultCartRepository
import woowacourse.shopping.data.cart.InMemoryCartRepository
import woowacourse.shopping.otterdi.Module
import woowacourse.shopping.otterdi.annotation.Inject
import woowacourse.shopping.otterdi.annotation.Qualifier
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class AppModule(context: Context) : Module {

    private val context = context.applicationContext

    fun provideProductRepository(): ProductRepository = DefaultProductRepository()

    fun provideCartProductDao(): CartProductDao =
        ShoppingDatabase.getDatabase(context).cartProductDao()

    @Qualifier("DefaultCartRepository")
    fun provideDatabaseCartRepository(@Inject cartProductDao: CartProductDao): CartRepository =
        DefaultCartRepository(cartProductDao)

    @Qualifier("InMemoryCartRepository")
    fun provideInMemoryCartRepository(): CartRepository {
        return InMemoryCartRepository()
    }
}
