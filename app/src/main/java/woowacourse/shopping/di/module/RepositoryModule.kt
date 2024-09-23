package woowacourse.shopping.di.module

import com.zzang.di.DIContainer
import com.zzang.di.annotation.QualifierType
import com.zzang.di.module.DIModule
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class RepositoryModule : DIModule {
    override fun register(container: DIContainer) {
        val cartProductDao = container.resolve(CartProductDao::class, QualifierType.DATABASE)

        container.registerModuleInstance(
            type = ProductRepository::class,
            instance = DefaultProductRepository(),
            qualifier = QualifierType.IN_MEMORY,
        )

        container.registerModuleInstance(
            type = CartRepository::class,
            instance = DefaultCartRepository(cartProductDao),
            qualifier = QualifierType.DATABASE,
        )

        container.registerModuleInstance(
            type = CartRepository::class,
            instance = InMemoryCartRepository(),
            qualifier = QualifierType.IN_MEMORY,
        )
    }
}
