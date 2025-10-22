package woowacourse.shopping.di

import com.example.di.ApplicationLifespan
import com.example.di.Dependency
import com.example.di.Module
import com.example.di.ViewModelLifespan
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class RepositoryModule : Module {
    @Dependency
    @ViewModelLifespan
    fun productRepository(): ProductRepository = DefaultProductRepository()

    @Dependency
    @ApplicationLifespan
    @DatabaseRepository
    fun databaseCartRepository(dao: CartProductDao): CartRepository = DatabaseCartRepository(dao)

    @Dependency
    @ApplicationLifespan
    @InMemoryRepository
    fun inMemoryCartRepository(): CartRepository = InMemoryCartRepository()
}
