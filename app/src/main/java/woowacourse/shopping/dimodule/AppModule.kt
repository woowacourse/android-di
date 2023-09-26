package woowacourse.shopping.dimodule

import android.content.Context
import androidx.room.Room
import com.bandal.fullmoon.FullMoonInject
import com.bandal.fullmoon.Qualifier
import com.bandal.halfmoon.AndroidDependencyModule
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DatabaseCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.data.repository.InMemoryProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import javax.inject.Singleton

class AppModule : AndroidDependencyModule {
    override var context: Context? = null

    fun createProductRepository(): ProductRepository = InMemoryProductRepository()

    @Singleton
    @Qualifier("databaseCartRepository")
    fun createCartDatabaseRepository(
        @FullMoonInject cartProductDao: CartProductDao,
    ): CartRepository = DatabaseCartRepository(cartProductDao)

    @Singleton
    @Qualifier("inMemoryProductRepository")
    fun createCartInMemoryRepository(): CartRepository = InMemoryCartRepository()

    @Singleton
    fun createCartProductDao(
        @FullMoonInject
        context: Context,
    ): CartProductDao {
        val database = Room
            .databaseBuilder(context, ShoppingDatabase::class.java, "shopping_database")
            .fallbackToDestructiveMigration()
            .build()
        return database.cartProductDao()
    }

    override fun getContext(): Context =
        context ?: throw NullPointerException("context가 초기화 되지 않았습니다.")
}
