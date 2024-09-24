package woowacourse.shopping.di

import android.content.Context
import com.example.yennydi.di.DependencyContainer
import com.example.yennydi.di.DependencyProvider
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.ui.cart.DateFormatter

class ShoppingActivityModule(private val context: Context) : DependencyProvider {
    override fun register(container: DependencyContainer) {
        addProductRepository(container)
        addDateFormatter(container)
    }

    private fun addProductRepository(container: DependencyContainer) {
        container.addDeferredDependency(
            ProductRepository::class to ProductRepositoryImpl::class,
        )
    }

    private fun addDateFormatter(container: DependencyContainer) {
        container.addInstance(DateFormatter::class, DateFormatter(context))
    }
}
