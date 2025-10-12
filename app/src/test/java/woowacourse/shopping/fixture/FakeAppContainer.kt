package woowacourse.shopping.fixture

import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.fixture.repository.FakeCartRepository
import woowacourse.shopping.fixture.repository.FakeProductRepository
import kotlin.reflect.KClass

class FakeAppContainer(
    productRepository: ProductRepository = FakeProductRepository(),
    cartRepository: CartRepository = FakeCartRepository(),
) : AppContainer {
    private val providers: Map<KClass<*>, Any> =
        mapOf(
            ProductRepository::class to productRepository,
            CartRepository::class to cartRepository,
        )

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(kClass: KClass<T>): T = providers[kClass] as T

    override fun <T : Any> canResolve(clazz: KClass<T>): Boolean = providers.containsKey(clazz)
}
