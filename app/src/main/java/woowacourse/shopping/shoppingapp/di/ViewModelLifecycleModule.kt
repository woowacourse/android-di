package woowacourse.shopping.shoppingapp.di

import com.woowacourse.di.DiContainer
import com.woowacourse.di.DiModule
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl

class ViewModelLifecycleModule : DiModule {
    fun bindProductRepository(diContainer: DiContainer) {
        diContainer.bind(
            ProductRepository::class,
            ProductRepositoryImpl::class,
        )
    }
}
