package woowacourse.shopping

import android.content.Context
import com.woowacourse.di.DependencyContainer
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
    fun initialize() {
        registerDateFormatter()
        registerProductRepository()
        registerCartRepository()
    }

    private fun registerDateFormatter() {
        DependencyContainer.addInstance(
            DateFormatter::class,
            DIModule.provideDateFormatter(context),
        )
    }

    private fun registerProductRepository() {
        DependencyContainer.addInstance(
            ProductRepository::class,
            DIModule.provideProductRepository(),
            qualifier = InMemory::class,
        )
    }

    private fun registerCartRepository() {
        DependencyContainer.addInstance(
            CartRepository::class,
            DIModule.provideCartRepository(cartProductDao),
            qualifier = RoomDB::class,
        )
        DependencyContainer.addInstance(
            CartRepository::class,
            DIModule.provideCartInMemoryRepository(),
            qualifier = InMemory::class,
        )
    }
}
