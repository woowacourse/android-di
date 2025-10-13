package woowacourse.shopping.di

import com.shopping.di.InjectContainer
import com.shopping.di.InjectionModule
import com.shopping.di.Provider
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class RepositoryModule : InjectionModule {
    override fun provideDefinitions(container: InjectContainer) {
        container.apply {
            registerSingleton<ProductRepository> { Provider { ProductRepositoryImpl() } }
            registerSingleton<CartRepository> { Provider { CartRepositoryImpl(get()) } }
        }
    }
}
