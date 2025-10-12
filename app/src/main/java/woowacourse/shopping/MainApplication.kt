package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.AppContainerStore
import woowacourse.shopping.di.DependencyFactory
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class MainApplication : Application() {
    lateinit var appContainerStore: AppContainerStore

    override fun onCreate() {
        super.onCreate()
        val db =
            Room
                .databaseBuilder(
                    applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping_db",
                ).build()
        appContainerStore =
            AppContainerStore(
                listOf(
                    DependencyFactory(CartRepository::class) {
                        DefaultCartRepository(
                            db.cartProductDao(),
                        )
                    },
                    DependencyFactory(ProductRepository::class) {
                        DefaultProductRepository()
                    },
                ),
            )
    }
}
