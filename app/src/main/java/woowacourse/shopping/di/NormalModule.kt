package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import com.di.berdi.Module
import com.di.berdi.annotation.Qualifier
import com.di.berdi.annotation.Singleton
import woowacourse.shopping.data.CartInMemoryRepository
import woowacourse.shopping.data.CartOnDiskRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ProductDefaultRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object NormalModule : Module {

    @Singleton
    @Qualifier(qualifiedName = "OnDisk")
    fun provideOnDiskCartRepository(cartProductDao: CartProductDao): CartRepository =
        CartOnDiskRepository(cartProductDao)

    @Singleton
    @Qualifier(qualifiedName = "InMemory")
    fun provideInMemoryCartRepository(): CartRepository = CartInMemoryRepository()

    fun provideProductRepository(): ProductRepository = ProductDefaultRepository()

    @Singleton
    fun provideCartProductDao(database: ShoppingDatabase): CartProductDao {
        return database.cartProductDao()
    }

    @Singleton
    fun provideShoppingDB(context: Context): ShoppingDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ShoppingDatabase::class.java,
            "shopping_db",
        ).build()
    }
}
