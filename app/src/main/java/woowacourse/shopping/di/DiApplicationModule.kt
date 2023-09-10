package woowacourse.shopping.di

import android.content.Context
import com.re4rk.arkdi.DiContainer
import com.re4rk.arkdi.InstanceHolder
import com.re4rk.arkdi.Qualifier
import woowacourse.shopping.data.CartInMemoryRepository
import woowacourse.shopping.data.ProductSampleRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class DiApplicationModule(
    private val applicationContext: Context,
) : DiContainer() {
    @Qualifier("ApplicationContext")
    fun provideApplicationContext(): Context = Cache.applicationContext.get {
        applicationContext
    }

    fun provideProductRepository(): ProductRepository = Cache.productRepository.get {
        ProductSampleRepository()
    }

    @Qualifier("CartInMemoryRepository")
    fun provideCartRepositoryInMemory(): CartRepository = Cache.cartRepositoryInMemory.get {
        CartInMemoryRepository()
    }

    fun provideShoppingDatabase(
        @Qualifier("ApplicationContext") context: Context,
    ): ShoppingDatabase = Cache.shoppingDatabase.get {
        ShoppingDatabase.getInstance(context)
    }

    private object Cache {
        val applicationContext = InstanceHolder<Context>()
        val productRepository = InstanceHolder<ProductRepository>()
        val cartRepositoryInMemory = InstanceHolder<CartRepository>()
        val shoppingDatabase = InstanceHolder<ShoppingDatabase>()
    }
}
