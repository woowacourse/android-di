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

        container.registerInstance(
            ProductRepository::class,
            DefaultProductRepository(),
            QualifierType.IN_MEMORY,
        )

        container.registerInstance(
            CartRepository::class,
            DefaultCartRepository(cartProductDao),
            QualifierType.DATABASE,
        )

        container.registerInstance(
            CartRepository::class,
            InMemoryCartRepository(),
            QualifierType.IN_MEMORY,
        )
    }
}
