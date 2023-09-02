package woowacourse.shopping.di

import woowacourse.shopping.data.ProductSampleRepository
import woowacourse.shopping.repository.ProductRepository

open class DiContainer {
    val productRepository: ProductRepository = ProductSampleRepository()

    inline fun <reified T> get(clazz: Class<T>): T {
        return if (clazz == ProductRepository::class.java) {
            productRepository as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
