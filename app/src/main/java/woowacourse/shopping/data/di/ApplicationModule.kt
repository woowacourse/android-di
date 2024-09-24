package woowacourse.shopping.data.di

import android.content.Context
import android.util.Log
import com.woowacourse.di.DependencyContainer
import com.woowacourse.di.Module
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.cart.DateFormatter

class ApplicationModule(private val context: Context) : Module {
    override fun install() {
        Log.d("ApplicationModule", "install")
        provideProductRepository()
        provideCartRepository()
    }

    override fun clear() {
        Log.d("ApplicationModule", "clear")
        DependencyContainer.removeInstance(DateFormatter::class)
    }

    private fun provideProductRepository() {
        DependencyContainer.addInstance(
            classType = ProductRepository::class,
            instance = DefaultProductRepository(),
        )
    }

    private fun provideCartRepository() {
        DependencyContainer.addInstance(
            classType = CartRepository::class,
            instance = DefaultCartRepository(
                ShoppingDatabase.instance(context).cartProductDao(),
            ),
            qualifier = DefaultCartRepository.QUALIFIER_NAME,
        )

        DependencyContainer.addInstance(
            classType = CartRepository::class,
            instance = InMemoryCartRepository(),
            qualifier = InMemoryCartRepository.QUALIFIER_NAME,
        )
    }
}
