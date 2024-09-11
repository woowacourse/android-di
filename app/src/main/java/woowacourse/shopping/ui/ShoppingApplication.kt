package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.ui.util.DependencyContainer
import woowacourse.shopping.ui.util.ViewModelDependencyContainer

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        viewModelDependencyContainer.setDependency(
            ProductRepository::class,
            DefaultProductRepository::class
        )
        viewModelDependencyContainer.setDependency(
            CartRepository::class,
            DefaultCartRepository::class
        )
    }

    companion object {
        val viewModelDependencyContainer: DependencyContainer by lazy {
            ViewModelDependencyContainer()
        }
    }
}
