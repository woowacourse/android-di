package woowacourse.shopping

import android.app.Application
import com.example.di.DIContainer
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        val db = ShoppingDatabase.getInstance(this)
        DIContainer.register(ProductRepository::class) { DefaultProductRepository() }
        DIContainer.register(CartRepository::class) {
            DefaultCartRepository(db.cartProductDao())
        }
    }
}
