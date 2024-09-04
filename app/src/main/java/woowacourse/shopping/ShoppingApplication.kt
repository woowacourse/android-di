package woowacourse.shopping

import woowacourse.shopping.di.ViewModelFactory
import android.app.Application
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class ShoppingApplication : Application() {
    private val dependencies: MutableMap<Class<out Any>, Any> = mutableMapOf()
    private val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl()
    }
    private val cartRepository: CartRepository by lazy {
        CartRepositoryImpl()
    }

    val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory(dependencies)
    }

    init {
        dependencies[ProductRepository::class.java] = productRepository
        dependencies[CartRepository::class.java] = cartRepository
    }
}
