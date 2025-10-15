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

class ShoppingApplication : Application() {
    private val appContainer: Container by lazy { Container() }
    private val dependencyInjector: DependencyInjector by lazy { DependencyInjector(appContainer) }
    val viewModelFactory: ViewModelFactory by lazy { ViewModelFactory(dependencyInjector) }

    private val database: ShoppingDatabase by lazy { ShoppingDatabase.getDatabase(this) }

    init {
        appContainer.registerProvider(ProductRepository::class) {
            ProductDefaultRepository()
        }
        appContainer.registerProvider(CartRepository::class, RepositoryType.ROOM_DB) {
            CartDatabaseRepository(database.cartProductDao())
        }
        appContainer.registerProvider(CartRepository::class, RepositoryType.IN_MEMORY) {
            CartInMemoryRepository()
        }
    }
}
