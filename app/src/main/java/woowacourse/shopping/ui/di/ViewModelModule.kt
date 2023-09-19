package woowacourse.shopping.ui.di

import io.hyemdooly.di.Module
import woowacourse.shopping.data.InMemoryProductRepository

class ViewModelModule(parentModule: Module) : Module(parentModule) {
    fun provideRepository() = InMemoryProductRepository()
}
