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
    private val container: Container = Container
    private val dependencyInjector: DependencyInjector by lazy {
        DependencyInjector(container)
    }

    val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory(dependencyInjector)
    }

    private val database: ShoppingDatabase by lazy { ShoppingDatabase.getDatabase(this) }

    init {
        container.register(ProductRepository::class) {
            ProductDefaultRepository()
        }
        container.register(CartRepository::class, RepositoryType.ROOM_DB) {
            CartDatabaseRepository(database.cartProductDao())
        }
        container.register(CartRepository::class, RepositoryType.IN_MEMORY) {
            CartInMemoryRepository()
        }
    }
}
