package woowacourse.shopping

import androidx.room.Room
import com.daedan.compactAndroidDi.DiApplication
import com.daedan.compactAndroidDi.module
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.RoomDBCartRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

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
                factory<CartRepository>(annotated<RoomDBCartRepository>()) {
                    DefaultCartRepository(
                        get(),
                    )
                }
                factory<ProductRepository>(named("productRepository")) {
                    DefaultProductRepository()
                }
                factory { CartViewModel(get(annotated<RoomDBCartRepository>())) }
                factory { MainViewModel() }
            },
        )
    }
}
