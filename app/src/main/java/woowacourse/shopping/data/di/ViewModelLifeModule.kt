package woowacourse.shopping.data.di

import com.android.di.component.DiContainer
import com.android.di.component.Module
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.ProductRepository

class ViewModelLifeModule : Module {
    fun bindProductRepository(diContainer: DiContainer) {
        diContainer.bind(
            ProductRepository::class,
            ProductRepositoryImpl::class,
        )
    }
}
