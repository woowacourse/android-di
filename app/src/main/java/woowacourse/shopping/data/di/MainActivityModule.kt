package woowacourse.shopping.data.di

import com.hyegyeong.di.DiModule
import woowacourse.shopping.data.repository.ProductRepository

class MainActivityModule : DiModule {

    fun provideInMemoryProductRepository(): ProductRepository =
        woowacourse.shopping.data.InMemoryProductRepository()
}
