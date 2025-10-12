package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KClass

object AppContainerImpl : AppContainer {
    private val providers: Map<KClass<*>, Any> =
        mapOf(
            ProductRepository::class to ProductRepositoryImpl(),
            CartRepository::class to CartRepositoryImpl(),
        )

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(kClass: KClass<T>): T =
        providers[kClass] as? T
            ?: throw IllegalArgumentException("${kClass.simpleName} 타입의 인스턴스가 등록되어 있지 않습니다.")

    override fun <T : Any> canResolve(clazz: KClass<T>): Boolean = providers.containsKey(clazz)
}
