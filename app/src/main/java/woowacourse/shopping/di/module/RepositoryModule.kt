package woowacourse.shopping.di.module

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.annotation.DatabaseRepository
import woowacourse.shopping.di.annotation.InMemoryRepository
import woowacourse.shopping.domain.CartRepository

class RepositoryModule : DIModule {
    override fun register(container: DIContainer) {
        // 이미 DatabaseModule에서 등록된 CartProductDao 인스턴스를 가져옴
        val cartProductDao = container.getInstance(CartProductDao::class, DatabaseRepository::class)

        // DefaultCartRepository 등록 및 매핑
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

        // InMemoryCartRepository 등록 및 매핑
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
