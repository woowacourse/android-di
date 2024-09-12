package woowacourse.shopping.model

import javax.inject.Singleton

@Singleton
interface ProductRepository {
    fun getAllProducts(): List<Product>
}
