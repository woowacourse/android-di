package woowacourse.shopping.di

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KType

interface Container {
    val productRepository: ProductRepository
    val cartRepository: CartRepository

    fun resolve(
        requestedType: KType,
        qualifier: Annotation?,
    ): Any?
}
