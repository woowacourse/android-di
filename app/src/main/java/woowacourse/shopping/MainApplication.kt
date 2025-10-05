package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.AppContainerStore
import woowacourse.shopping.di.DependencyFactory
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class MainApplication : Application() {
    val appContainerStore =
        AppContainerStore(
            listOf(
                DependencyFactory(CartRepository::class) {
                    DefaultCartRepository()
                },
                DependencyFactory(ProductRepository::class) {
                    DefaultProductRepository()
                },
            ),
        )
}
