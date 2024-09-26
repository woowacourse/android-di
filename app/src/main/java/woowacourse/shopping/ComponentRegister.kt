package woowacourse.shopping

import android.content.Context
import com.woowacourse.di.InMemory
import com.woowacourse.di.RoomDB
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter

class ComponentRegister(
    private val context: Context,
    private val cartProductDao: CartProductDao,
) {
    private val dependencyInjector = ShoppingApplication.dependencyContainer

    fun initialize() {
        registerDateFormatter()
        registerProductRepository()
        registerCartRepository()
    }

    private fun registerDateFormatter() {
        dependencyInjector.addInstance(
            DateFormatter::class,
            DIModule.provideDateFormatter(context),
        )
    }

    private fun registerProductRepository() {
        dependencyInjector.addInstance(
            ProductRepository::class,
            DIModule.provideProductRepository(),
            qualifier = InMemory::class,
        )
    }

    private fun registerCartRepository() {
        dependencyInjector.addInstance(
            CartRepository::class,
            DIModule.provideCartRepository(cartProductDao),
            qualifier = RoomDB::class,
        )
        dependencyInjector.addInstance(
            CartRepository::class,
            DIModule.provideCartInMemoryRepository(),
            qualifier = InMemory::class,
        )
    }
}
