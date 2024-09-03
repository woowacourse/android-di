package woowacourse.shopping.domain.repository

import woowacourse.shopping.domain.model.Product

interface CartRepository {
    fun addCartProduct(product: Product)

    fun getAllCartProducts(): List<Product>

    fun deleteCartProduct(id: Int)
}