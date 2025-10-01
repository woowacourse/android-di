package woowacourse.shopping.di

import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import kotlin.reflect.KClass

interface AppContainer {
    val productRepository: ProductRepository
    val cartRepository: CartRepository

    fun <T : Any> getInstance(clazz: KClass<T>): T
}