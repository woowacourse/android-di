package woowacourse.shopping.data.di

import com.woowacourse.di.DependencyContainer
import com.woowacourse.di.Module
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

class ViewModelModule : Module {
    override fun install() {
        provideProductRepository()
    }

    override fun clear() {
        DependencyContainer.removeInstance(ProductRepository::class)
    }

    private fun provideProductRepository() {
        DependencyContainer.addInstance(
            classType = ProductRepository::class,
            instance = DefaultProductRepository(),
        )
    }
}
