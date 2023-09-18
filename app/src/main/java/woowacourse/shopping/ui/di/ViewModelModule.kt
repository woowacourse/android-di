package woowacourse.shopping.ui.di

import io.hyemdooly.di.Module
import io.hyemdooly.di.annotation.Singleton
import woowacourse.shopping.data.InMemoryProductRepository

class ViewModelModule(parentModule: Module) : Module(parentModule) {
    @Singleton
    fun provideRepository() = InMemoryProductRepository()
}
