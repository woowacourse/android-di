package woowacourse.shopping.di.auto

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase

object AppContainer : Container() {
    fun init(context: Context) {
        bindSingleton(Context::class) { context.applicationContext }

        bindSingleton(ShoppingDatabase::class) {
            Room
                .databaseBuilder(
                    get(Context::class),
                    ShoppingDatabase::class.java,
                    "shopping.db",
                ).build()
        }

        bindSingleton(CartProductDao::class) {
            get(ShoppingDatabase::class).cartProductDao()
        }

        bindSingleton(CartRepository::class) {
            CartRepositoryImpl(get(CartProductDao::class))
        }
        bindSingleton(ProductRepository::class) {
            ProductRepositoryImpl()
        }
    }
}
