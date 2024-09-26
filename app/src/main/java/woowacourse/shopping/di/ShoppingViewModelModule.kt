package woowacourse.shopping.di

import com.example.yennydi.di.DependencyContainer
import com.example.yennydi.di.DependencyProvider
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl

class ShoppingViewModelModule : DependencyProvider {
    override fun register(container: DependencyContainer) {
        addProductRepository(container)
    }

    private fun addProductRepository(container: DependencyContainer) {
        container.addDeferredDependency(
            ProductRepository::class to ProductRepositoryImpl::class,
        )
    }
}
