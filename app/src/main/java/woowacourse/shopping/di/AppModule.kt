package woowacourse.shopping.di

import android.content.Context
import woowacourse.di.annotation.InjectField
import woowacourse.di.annotation.Singleton
import woowacourse.di.module.AndroidModule
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.InDiskCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

class AppModule : AndroidModule {

    override lateinit var context: Context

    override fun setModuleContext(context: Context) {
        this.context = context
    }

    fun provideInMemoryProductRepository(): ProductRepository = InMemoryProductRepository()

    @Singleton
    fun provideInMemoryCartRepository(): CartRepository = InMemoryCartRepository()

    @Singleton
    fun provideInDiskCartRepository(@InjectField cartDao: CartProductDao): CartRepository =
        InDiskCartRepository(cartDao)

    fun provideCartProductDao(): CartProductDao =
        ShoppingDatabase.getDatabase(context).cartProductDao()
}
