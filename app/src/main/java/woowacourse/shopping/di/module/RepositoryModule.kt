package woowacourse.shopping.di.module

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import com.zzang.di.DIContainer
import com.zzang.di.annotation.DatabaseRepository
import com.zzang.di.annotation.InMemoryRepository
import com.zzang.di.module.DIModule
import woowacourse.shopping.domain.CartRepository

class RepositoryModule : DIModule {
    override fun register(container: DIContainer) {
        val cartProductDao = container.getInstance(CartProductDao::class, DatabaseRepository::class)

        container.registerInstance(
            CartRepository::class,
            DefaultCartRepository(cartProductDao),
            DatabaseRepository::class,
        )
        container.registerInterfaceMapping(
            CartRepository::class,
            DefaultCartRepository::class,
            DatabaseRepository::class,
        )

        container.registerInstance(
            CartRepository::class,
            InMemoryCartRepository(),
            InMemoryRepository::class,
        )
        container.registerInterfaceMapping(
            CartRepository::class,
            InMemoryCartRepository::class,
            InMemoryRepository::class,
        )
    }
}
