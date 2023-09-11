package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.StartInjection
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        StartInjection {
            single<CartRepository>(DefaultCartRepository())
            single<ProductRepository>(DefaultProductRepository())
        }
    }
}
