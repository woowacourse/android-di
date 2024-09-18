package woowacourse.shopping.di.repository

import com.woowa.di.component.InstallIn
import com.woowa.di.singleton.SingletonComponent
import com.woowa.di.viewmodel.ViewModelComponent
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.isSubclassOf


@InstallIn(ViewModelComponent::class)
class RepositoryBinder {
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()
}

@InstallIn(SingletonComponent::class)
class RepositoryBinder2 {
    fun provideCartRepository(): CartRepository = CartRepositoryImpl()
}
