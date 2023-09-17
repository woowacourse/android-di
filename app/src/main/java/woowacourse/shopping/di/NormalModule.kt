package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import com.di.berdi.Module
import woowacourse.shopping.data.CartInMemoryRepository
import woowacourse.shopping.data.CartOnDiskRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ProductDefaultRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object NormalModule : Module {

    @OnDisk
    fun provideInMemoryCartRepository(cartProductDao: CartProductDao): CartRepository =
        CartOnDiskRepository(cartProductDao)

    @InMemory
    fun provideInMemoryCartRepository(): CartRepository = CartInMemoryRepository()

    fun provideProductRepository(): ProductRepository = ProductDefaultRepository()

    fun provideCartProductDao(database: ShoppingDatabase): CartProductDao {
        return database.cartProductDao()
    }

    fun provideShoppingDB(context: Context): ShoppingDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ShoppingDatabase::class.java,
            "shopping_db",
        ).build()
    }
}
