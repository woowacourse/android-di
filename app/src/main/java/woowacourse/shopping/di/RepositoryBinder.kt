package woowacourse.shopping.di

import com.woowa.di.component.InstallIn
import com.woowa.di.singleton.SingletonComponent
import com.woowa.di.viewmodel.ViewModelComponent
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository
import javax.inject.Qualifier

@InstallIn(ViewModelComponent::class)
object ViewModelRepositoryBinder {
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()
}

@InstallIn(SingletonComponent::class)
object SingletonRepositoryBinder {
    fun provideCartRepository(@Database dao: CartProductDao): CartRepository = CartRepositoryImpl(dao)
}
