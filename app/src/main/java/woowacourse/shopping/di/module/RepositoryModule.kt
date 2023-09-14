package woowacourse.shopping.di.module

import android.content.Context
import com.app.covi_di.module.DependencyModule
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import kotlin.reflect.KClass

object RepositoryModule: DependencyModule {
    override fun invoke(): Map<KClass<*>, KClass<*>> {
        return mapOf(
            ProductRepository::class to ProductRepositoryImpl::class,
            CartRepository::class to CartRepositoryImpl::class
        )
    }
}