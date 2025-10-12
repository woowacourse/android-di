package woowacourse.shopping

import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.DiApplication
import woowacourse.shopping.di.module
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class MainApplication : DiApplication() {
    override fun onCreate() {
        super.onCreate()
        register(
            module {
                factory {
                    Room
                        .databaseBuilder(
                            applicationContext,
                            ShoppingDatabase::class.java,
                            "shopping_db",
                        ).build()
                }
                factory { get<ShoppingDatabase>().cartProductDao() }
                factory<CartRepository> { DefaultCartRepository(get()) }
                factory<ProductRepository> {
                    DefaultProductRepository()
                }
            },
        )
    }
}
