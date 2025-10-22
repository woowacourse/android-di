package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KClass

class AppContainer {
    private val providers = mutableMapOf<KClass<*>, () -> Any>()

    init {
        providers[CartRepository::class] = { DefaultCartRepository() }
        providers[ProductRepository::class] = { DefaultProductRepository() }
    }

    private val instances = mutableMapOf<KClass<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(clazz: KClass<T>): T {
        instances[clazz]?.let { return it as T }

        val factory =
            providers[clazz]
                ?: throw IllegalArgumentException("${clazz.simpleName} provider 없음")

        // 인스턴스 생성 및 캐싱
        val instance = factory() as T
        instances[clazz] = instance
        return instance
    }
}
