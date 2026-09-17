package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.reflect.KClass

object DependencyContainer {
    private val dependencies: Map<KClass<*>, Any> =
        mapOf(
            ProductRepository::class to ProductRepository(),
            CartRepository::class to CartRepository(),
        )

    fun get(type: KClass<*>): Any =
        dependencies[type]
            ?: error("등록되지 않은 의존성입니다: ${type.qualifiedName}")
}
