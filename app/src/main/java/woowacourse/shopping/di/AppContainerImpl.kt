package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import kotlin.reflect.KClass

object AppContainerImpl : AppContainer {
    override val productRepository: ProductRepository by lazy { ProductRepositoryImpl() }
    override val cartRepository: CartRepository by lazy { CartRepositoryImpl() }

    private val instances: Map<KClass<*>, Any> =
        mapOf(
            ProductRepository::class to productRepository,
            CartRepository::class to cartRepository,
        )

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getInstance(clazz: KClass<T>): T = instances[clazz] as? T ?: throw IllegalArgumentException("존재하지 않는 클래스입니다.")
}
