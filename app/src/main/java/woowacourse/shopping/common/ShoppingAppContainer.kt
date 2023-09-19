package woowacourse.shopping.common

import android.content.Context
import androidx.room.Room
import com.bandal.fullmoon.AppContainer
import com.bandal.fullmoon.Qualifier
import com.bandal.fullmoon.SingleTone
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DatabaseCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.data.repository.InMemoryProductRepository
import woowacourse.shopping.ui.cart.DateFormatter

class ShoppingAppContainer(private val context: Context) : AppContainer() {

    @SingleTone
    fun createDateFormatter(context: Context): DateFormatter {
        return DateFormatter(context)
    }

    @Qualifier("inMemoryProductRepository")
    fun createInMemoryProductRepository(): InMemoryProductRepository {
        return InMemoryProductRepository()
    }

    @SingleTone
    @Qualifier("databaseCartRepository")
    fun createDatabaseCartRepository(cartProductDao: CartProductDao): DatabaseCartRepository {
        return DatabaseCartRepository(
            cartProductDao,
        )
    }

    @Qualifier("inMemoryCartRepository")
    fun createInMemoryCartRepository(): InMemoryCartRepository {
        return InMemoryCartRepository()
    }

    fun createCartProductDao(): CartProductDao {
        val database = Room
            .databaseBuilder(context, ShoppingDatabase::class.java, "shopping_database")
            .fallbackToDestructiveMigration()
            .build()
        return database.cartProductDao()
    }
}
