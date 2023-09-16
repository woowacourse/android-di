package woowacourse.shopping.di

import android.content.Context
import woowacourse.di.Module
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.InDiskCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

class AppModule(private val context: Context) : Module {

    fun provideInMemoryProductRepository(): ProductRepository = InMemoryProductRepository()

    fun provideInMemoryCartRepository(): CartRepository = InMemoryCartRepository()

    fun provideInDiskCartRepository(): CartRepository =
        InDiskCartRepository(provideCartProductDao())

    fun provideCartProductDao(): CartProductDao =
        ShoppingDatabase.getDatabase(context).cartProductDao()
}
