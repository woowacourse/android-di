package woowacourse.shopping.model

interface ProductRepository {

    fun getAllProducts(): List<Product>
}
