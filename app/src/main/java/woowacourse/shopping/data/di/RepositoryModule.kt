package woowacourse.shopping.data.di

import android.content.Context
import com.woowacourse.di.DependencyContainer
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase

class RepositoryModule(private val context: Context) {
    fun install() {
        provideProductRepository()
        provideCartRepository()
    }

    private fun provideProductRepository() {
        DependencyContainer.addInstance(
            ProductRepository::class,
            DefaultProductRepository(),
        )
    }

    private fun provideCartRepository() {
        DependencyContainer.addInstance(
            CartRepository::class,
            DefaultCartRepository(
                ShoppingDatabase.instance(context).cartProductDao(),
            ),
            DefaultCartRepository.QUALIFIER_NAME,
        )

        DependencyContainer.addInstance(
            CartRepository::class,
            InMemoryCartRepository(),
            InMemoryCartRepository.QUALIFIER_NAME,
        )
    }
}
