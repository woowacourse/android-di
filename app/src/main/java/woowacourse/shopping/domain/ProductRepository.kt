package woowacourse.shopping.domain

interface ProductRepository {
    fun getAllProducts(): List<Product>
}
