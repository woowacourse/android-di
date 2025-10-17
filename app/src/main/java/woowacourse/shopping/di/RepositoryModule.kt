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
    private val shoppingDatabase: ShoppingDatabase = ShoppingDatabase.Companion.instance(context)

    @Dependency
    @ViewModelLifespan
    val productRepository: ProductRepository by lazy { DefaultProductRepository() }

    @Dependency
    @ApplicationLifespan
    @DatabaseRepository
    val databaseCartRepository: CartRepository by lazy { DatabaseCartRepository(shoppingDatabase.cartProductDao()) }

    @Dependency
    @ApplicationLifespan
    @InMemoryRepository
    val inMemoryCartRepository: CartRepository by lazy { InMemoryCartRepository() }
}
