package woowacourse.shopping.di

import android.content.Context
import com.re4rk.arkdi.DiContainer
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
    fun provideApplicationContext(): Context = applicationContext

    fun provideProductRepository(): ProductRepository = ProductSampleRepository()

    @StorageType(IN_MEMORY)
    fun provideCartRepositoryInMemory(): CartRepository = CartInMemoryRepository()

    fun provideShoppingDatabase(
        @ContextType(APPLICATION) context: Context,
    ): ShoppingDatabase = ShoppingDatabase.getInstance(context)
}
