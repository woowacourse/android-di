package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import kotlin.reflect.KClass

object RepositoryModule: DependencyModule {
    override fun invoke(context: Context): Map<KClass<*>, KClass<*>> {
        return mapOf(
            ProductRepository::class to ProductRepositoryImpl::class,
            CartRepository::class to CartRepositoryImpl::class
        )
    }

}