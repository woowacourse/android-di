package woowacourse.shopping.data.di

import android.util.Log
import com.android.di.component.DiSingletonComponent
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

object RepositoryModule {
    fun install() {
        bindCartRepository()
        bindProductRepository()
    }

    private fun bindCartRepository() {
        DiSingletonComponent.bind(
            CartRepository::class,
            CartRepositoryImpl::class,
            logging = {
                Log.d("DI logging",it)
            }
        )
    }

    private fun bindProductRepository() {
        DiSingletonComponent.bind(
            ProductRepository::class,
            ProductRepositoryImpl::class,
            logging = {
                Log.d("DI logging",it)
            }
        )
    }
}
