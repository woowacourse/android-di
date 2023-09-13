package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartInMemoryRepository
import woowacourse.shopping.data.CartOnDiskRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ProductDefaultRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.di.annotation.OnDisk
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object NormalModule : Module {

    @OnDisk
    fun provideInMemoryCartRepository(cartProductDao: CartProductDao): CartRepository =
        CartOnDiskRepository(cartProductDao)

    @InMemory
    fun provideInMemoryCartRepository(): CartRepository = CartInMemoryRepository()

    fun provideProductRepository(): ProductRepository = ProductDefaultRepository()

    fun provideCartProductDao(context: Context): CartProductDao {
        val db = Room.databaseBuilder(
            context.applicationContext,
            ShoppingDatabase::class.java,
            "shopping_db",
        ).build()
        return db.cartProductDao()
    }
}
