package woowacourse.shopping

import android.app.Application
import com.example.di.DIContainer
import com.example.di.Remote
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        val db = ShoppingDatabase.getInstance(this)
        DIContainer.register(ProductRepository::class, Remote::class) { DefaultProductRepository() }
        DIContainer.register(CartRepository::class, Remote::class) {
            DefaultCartRepository(db.cartProductDao())
        }
    }
}
