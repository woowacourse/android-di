package woowacourse.shopping.di

import com.woowa.di.component.InstallIn
import com.woowa.di.singleton.SingletonComponent2
import com.woowa.di.viewmodel.ViewModelComponent2
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

@InstallIn(ViewModelComponent2::class)
class ViewModelRepositoryBinder {
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()
}

@InstallIn(SingletonComponent2::class)
class SingletonRepositoryBinder {
    fun provideCartRepository(): CartRepository = CartRepositoryImpl()
}
