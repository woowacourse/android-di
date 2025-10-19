package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.CartDatabaseRepository
import woowacourse.shopping.data.repository.CartInMemoryRepository
import woowacourse.shopping.data.repository.ProductDefaultRepository
import woowacourse.shopping.data.repository.RepositoryType
import woowacourse.shopping.di.Container
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.ViewModelFactory
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter

class ShoppingApplication : Application() {
    val appContainer: Container by lazy { Container() }
    val dependencyInjector: DependencyInjector by lazy { DependencyInjector(appContainer) }
    val viewModelFactory: ViewModelFactory by lazy { ViewModelFactory(dependencyInjector) }

    private val database: ShoppingDatabase by lazy { ShoppingDatabase.getDatabase(this) }

    init {
        appContainer.registerProvider(kClass = ProductRepository::class) {
            ProductDefaultRepository()
        }
        appContainer.registerProvider(
            kClass = CartRepository::class,
            qualifier = RepositoryType.ROOM_DB,
        ) {
            CartDatabaseRepository(database.cartProductDao())
        }
        appContainer.registerProvider(
            kClass = CartRepository::class,
            qualifier = RepositoryType.IN_MEMORY,
        ) {
            CartInMemoryRepository()
        }
        appContainer.registerProvider(kClass = DateFormatter::class) {
            DateFormatter(this)
        }
    }
}
