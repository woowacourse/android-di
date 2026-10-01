package woowacourse.shopping

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.Provides
import woowacourse.shopping.model.DeliveryFee

class AppModule(
    private val context: Context
) {
    @Provides
    fun provideShoppingDatabase(): ShoppingDatabase {
        return Room.databaseBuilder(context, ShoppingDatabase::class.java, "di.db").build()
    }

    @Provides
    fun provideCartProductDao(database: ShoppingDatabase): CartProductDao = database.cartProductDao()

    @Provides
    fun provideDefaultCartRepository(dao: CartProductDao): CartRepository {
        return DefaultCartRepository(dao)
    }

    @Provides
    fun provideDeliveryFee(): DeliveryFee = DeliveryFee(3_000)
}
