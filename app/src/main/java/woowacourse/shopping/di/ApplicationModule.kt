package woowacourse.shopping.di

import android.content.Context
import com.lope.di.container.DependencyContainer
import com.lope.di.inject.CustomInjector
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ApplicationModule(applicationContext: Context, customInjector: CustomInjector) {

    init {
        DependencyContainer.setInstance(
            CartProductDao::class,
            ShoppingDatabase.getDatabase(applicationContext).cartProductDao(),
        )
        DependencyContainer.setInstance(
            ProductRepository::class,
            customInjector.inject(InMemoryProductRepository::class),
        )
        DependencyContainer.setInstance(
            CartRepository::class,
            customInjector.inject(DatabaseCartRepository::class),
        )
        DependencyContainer.setInstance(
            CartRepository::class,
            customInjector.inject(InMemoryCartRepository::class),
        )
    }
}
