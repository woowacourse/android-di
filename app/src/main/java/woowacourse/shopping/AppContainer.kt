package woowacourse.shopping

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository
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
