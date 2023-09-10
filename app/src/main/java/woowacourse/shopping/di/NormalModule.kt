package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartDefaultRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ProductDefaultRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object NormalModule : Module {
    fun provideCartRepository(cartProductDao: CartProductDao): CartRepository =
        CartDefaultRepository(cartProductDao)

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
