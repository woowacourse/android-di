package woowacourse.shopping.di

import android.content.Context
import com.example.di.ApplicationLifespan
import com.example.di.Dependency
import com.example.di.Module
import com.example.di.ViewModelLifespan
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class RepositoryModule(
    context: Context,
) : Module {
    private val shoppingDatabase: ShoppingDatabase = ShoppingDatabase.instance(context)

    @Dependency
    @ViewModelLifespan
    fun productRepository(): ProductRepository = DefaultProductRepository()

    @Dependency
    @ApplicationLifespan
    @DatabaseRepository
    fun databaseCartRepository(): CartRepository = DatabaseCartRepository(shoppingDatabase.cartProductDao())

    @Dependency
    @ApplicationLifespan
    @InMemoryRepository
    fun inMemoryCartRepository(): CartRepository = InMemoryCartRepository()
}
