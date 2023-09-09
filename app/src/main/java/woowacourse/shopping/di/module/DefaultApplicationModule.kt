package woowacourse.shopping.di.module

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ShoppingDatabase

class DefaultApplicationModule(applicationContext: Context) :
    ApplicationModule(applicationContext) {
    fun getCartRepository(cartProductDao: CartProductDao): CartRepository {
        return getOrCreateInstance { DefaultCartRepository(cartProductDao) }
    }

    fun getCartDao(): CartProductDao {
        return getOrCreateInstance {
            val applicationContext =
                context ?: throw IllegalStateException("애플리케이션이 아직 초기화되지 않았습니다")
            Room.databaseBuilder(
                applicationContext,
                ShoppingDatabase::class.java,
                SHOPPING_DATABASE_NAME,
            ).build().cartProductDao()
        }
    }

    companion object {
        private const val SHOPPING_DATABASE_NAME = "shopping_db_name"
    }
}
