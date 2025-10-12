package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KClass

class AppContainerImpl(
    context: Context,
) : AppContainer {
    private val database: ShoppingDatabase by lazy { ShoppingDatabase.getDatabase(context) }
    private val providers: Map<KClass<*>, Any> =
        mapOf(
            ProductRepository::class to ProductRepositoryImpl(),
            CartRepository::class to CartRepositoryImpl(database.cartProductDao()),
        )

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(kClass: KClass<T>): T =
        providers[kClass] as? T
            ?: throw IllegalArgumentException("${kClass.simpleName} 타입의 인스턴스가 등록되어 있지 않습니다.")

    override fun <T : Any> canResolve(clazz: KClass<T>): Boolean = providers.containsKey(clazz)
}
