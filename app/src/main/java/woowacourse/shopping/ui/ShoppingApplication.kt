package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.repository.CartDefaultRepository
import woowacourse.shopping.data.repository.ProductDefaultRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.ui.util.DependencyContainer
import woowacourse.shopping.ui.util.ViewModelDependencyContainer

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        viewModelDependencyContainer.setDependency(
            ProductRepository::class,
            ProductDefaultRepository::class
        )
        viewModelDependencyContainer.setDependency(
            CartRepository::class,
            CartDefaultRepository::class
        )
    }

    companion object {
        val viewModelDependencyContainer: DependencyContainer by lazy {
            ViewModelDependencyContainer()
        }
    }
}
