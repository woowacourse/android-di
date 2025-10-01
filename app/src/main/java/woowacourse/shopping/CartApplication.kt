package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.DiContainer
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class CartApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        postInstances()
    }

    private fun postInstances() {
        DiContainer.postInstance(CartRepository::class, DefaultCartRepository())
        DiContainer.postInstance(ProductRepository::class, DefaultProductRepository())
    }
}