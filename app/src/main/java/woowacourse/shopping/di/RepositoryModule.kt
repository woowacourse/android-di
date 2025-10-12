package woowacourse.shopping.di

import android.content.Context
import com.example.di.Dependency
import com.example.di.Module
import com.example.di.Qualifier
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

@Target(AnnotationTarget.PROPERTY)
@Qualifier
annotation class DatabaseRepository

@Target(AnnotationTarget.PROPERTY)
@Qualifier
annotation class InMemoryRepository

class RepositoryModule(
    context: Context,
) : Module {
    private val shoppingDatabase: ShoppingDatabase = ShoppingDatabase.instance(context)

    @Dependency
    val productRepository: ProductRepository by lazy { DefaultProductRepository() }

    @Dependency
    @DatabaseRepository
    val databaseCartRepository: CartRepository by lazy { DatabaseCartRepository(shoppingDatabase.cartProductDao()) }

    @Dependency
    @InMemoryRepository
    val inMemoryCartRepository: CartRepository by lazy { InMemoryCartRepository() }
}
