package woowacourse.shopping.data.repository

import woowacourse.shopping.model.Product

interface CartRepository {

    fun addCartProduct(product: Product)

    fun getAllCartProducts(): List<Product>

    fun deleteCartProduct(id: Int)
}