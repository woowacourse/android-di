package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import kotlin.reflect.KClass

class AppContainerImpl(
    context: Context,
) : AppContainer {
    private val database: ShoppingDatabase by lazy { ShoppingDatabase.getDatabase(context) }
    private val providers: MutableMap<KClass<*>, Any> = mutableMapOf()

    init {
        register(ProductRepository::class, ProductRepositoryImpl())
        register(CartRepository::class, CartRepositoryImpl(database.cartProductDao()))
    }

    override fun <T : Any> register(
        kClass: KClass<T>,
        instance: T,
    ) {
        providers[kClass] = instance
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(kClass: KClass<T>): T =
        providers[kClass] as? T
            ?: throw IllegalArgumentException("${kClass.simpleName} 타입의 인스턴스가 등록되어 있지 않습니다.")

    override fun <T : Any> canResolve(clazz: KClass<T>): Boolean = providers.containsKey(clazz)
}
