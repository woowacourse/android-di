package woowacourse.shopping

import android.app.Application
import com.buna.di.dsl.modules
import com.buna.di.dsl.types
import woowacourse.shopping.data.repository.DatabaseCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.common.di.module.RepositoryModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        types {
            type(ProductRepository::class to DefaultProductRepository::class)
            type(CartRepository::class to InMemoryCartRepository::class)
            type(CartRepository::class to DatabaseCartRepository::class)
        }
        modules {
            module(RepositoryModule(this@ShoppingApplication))
        }
    }
}
