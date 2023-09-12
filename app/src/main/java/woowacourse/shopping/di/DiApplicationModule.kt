package woowacourse.shopping.di

import android.content.Context
import com.re4rk.arkdi.DiContainer
import com.re4rk.arkdi.InstanceHolder
import woowacourse.shopping.data.CartInMemoryRepository
import woowacourse.shopping.data.ProductSampleRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.ContextType.Type.APPLICATION
import woowacourse.shopping.di.StorageType.Type.IN_MEMORY
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class DiApplicationModule(
    private val applicationContext: Context,
) : DiContainer() {
    @ContextType(APPLICATION)
    fun provideApplicationContext(): Context = Cache.applicationContext.get {
        applicationContext
    }

    fun provideProductRepository(): ProductRepository = Cache.productRepository.get {
        ProductSampleRepository()
    }

    @StorageType(IN_MEMORY)
    fun provideCartRepositoryInMemory(): CartRepository = Cache.cartRepositoryInMemory.get {
        CartInMemoryRepository()
    }

    fun provideShoppingDatabase(
        @ContextType(APPLICATION) context: Context,
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
