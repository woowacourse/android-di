package woowacourse.shopping

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import kotlin.reflect.KClass

class AppContainer {
    private val providers = mutableMapOf<KClass<*>, Any>()

    init {
        providers[CartRepository::class] = CartRepositoryImpl()
        providers[ProductRepository::class] = ProductRepositoryImpl()
    }

    fun <T : Any> get(clazz: KClass<T>): T =
        providers[clazz] as? T
            ?: throw IllegalArgumentException("${clazz.simpleName} provider를 찾을 수 없습니다")
}
