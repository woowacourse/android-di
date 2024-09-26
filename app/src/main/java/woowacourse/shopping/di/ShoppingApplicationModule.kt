package woowacourse.shopping.di

import android.content.Context
import com.example.yennydi.di.DependencyContainer
import com.example.yennydi.di.DependencyProvider
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DataBaseCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingApplicationModule(private val context: Context) : DependencyProvider {
    override fun register(container: DependencyContainer) {
        addCartRepository(container)
        addCartDao(container)
    }

    private fun addCartRepository(container: DependencyContainer) {
        container.addDeferredDependency(
            CartRepository::class to DataBaseCartRepository::class,
            CartRepository::class to InMemoryCartRepository::class,
        )
    }

    private fun addCartDao(container: DependencyContainer) {
        val db = ShoppingDatabase.getInstance(context)
        container.addInstance(
            CartProductDao::class,
            db.cartProductDao(),
        )
    }
}
