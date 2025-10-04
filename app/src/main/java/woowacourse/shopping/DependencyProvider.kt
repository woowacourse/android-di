package woowacourse.shopping

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KType
import kotlin.reflect.typeOf

object DependencyProvider {
    private val Dependencies: Map<KType, Lazy<Any>> =
        mapOf(
            typeOf<ProductRepository>() to lazy { DefaultProductRepository() },
            typeOf<CartRepository>() to lazy { DefaultCartRepository() },
        )

    fun dependency(key: KType): Any = Dependencies[key]?.value ?: error("${key}에 대한 의존성이 정의되지 않았습니다.")
}
