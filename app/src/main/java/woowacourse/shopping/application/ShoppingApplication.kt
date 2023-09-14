package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.data.database.ShoppingDatabase
import woowacourse.shopping.data.repository.DatabaseCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.di.sgDi.StartInjection
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.util.annotation.WooWaQualifier

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val dao = ShoppingDatabase.from(this).cartProductDao()

        StartInjection {
            single<CartRepository>(WooWaQualifier.DATABASE, DatabaseCartRepository(dao))
            single<CartRepository>(WooWaQualifier.IN_MEMORY, InMemoryCartRepository())
            single<ProductRepository>(DefaultProductRepository())
        }
    }
}
