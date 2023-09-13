package woowacourse.shopping.data.repository

import com.example.bbottodi.di.annotation.InMemory
import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository

@InMemory
class InMemoryCartRepository : CartRepository {

    private val cartProducts: MutableList<Product> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList().map { it.toEntity().toCartProduct() }
    }

    override suspend fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
