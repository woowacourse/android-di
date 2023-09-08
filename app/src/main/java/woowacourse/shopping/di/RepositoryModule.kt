package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import kotlin.reflect.KClass

object RepositoryModule: DependencyModule {
    override fun invoke(): Map<KClass<*>, Any> {
        return mapOf(
            ProductRepository::class to ProductRepositoryImpl(),
            CartRepository::class to CartRepositoryImpl()
        )
    }
}