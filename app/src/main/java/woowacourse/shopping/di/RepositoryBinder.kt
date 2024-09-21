package woowacourse.shopping.di

import com.woowa.di.component.InstallIn
import com.woowa.di.singleton.SingletonComponent
import com.woowa.di.viewmodel.ViewModelComponent
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

@InstallIn(ViewModelComponent::class)
class ViewModelRepositoryBinder {
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()
}

@InstallIn(SingletonComponent::class)
class SingletonRepositoryBinder {
    fun provideCartRepository(): CartRepository = CartRepositoryImpl()
}
