package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.DependencyRegistry
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class AndroidDiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val diContainer = DependencyRegistry
        diContainer.addInstance(ProductRepository::class, DefaultProductRepository())
        diContainer.addInstance(CartRepository::class, DefaultCartRepository())
    }
}
