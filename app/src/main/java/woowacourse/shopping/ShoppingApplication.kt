package woowacourse.shopping

import com.example.sh1mj1.DiApplication
import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.activityscope.activityScopeComponent
import com.example.sh1mj1.component.singleton.singletonComponent
import com.example.sh1mj1.component.viewmodelscope.viewModelScopeComponent
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.cart.KoreaLocaleDateFormatter

class ShoppingApplication : DiApplication() {
    override fun onCreate() {
        super.onCreate()

        container.add(
            singletonComponent<CartProductDao>(CartProductDao.instance(this), Qualifier("RoomDao", generate = true)),
        )

        container.add(
            singletonComponent<CartRepository>(InMemoryCartRepository(), Qualifier("InMemory")),
            singletonComponent<CartRepository>(
                DefaultCartRepository(),
                Qualifier("RoomDao"),
            ),
        )

        container.add(
            viewModelScopeComponent<ProductRepository>(
                InMemoryProductRepository(),
                Qualifier("InMemory"),
            ),
        )

        activityContainer.add(
            activityScopeComponent<DateFormatter>({ context -> KoreaLocaleDateFormatter(context) }),
        )
    }
}
