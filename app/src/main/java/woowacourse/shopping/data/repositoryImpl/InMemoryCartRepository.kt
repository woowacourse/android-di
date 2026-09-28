package woowacourse.shopping.data.repositoryImpl

import com.harodi.Qualifier
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProduct> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toCartProduct())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        val cartProduct = cartProducts.firstOrNull { it.id == id } ?: throw IllegalArgumentException("ID가 존재하지 않는 장바구니 상품입니다.")
        cartProducts.remove(cartProduct)
    }
}

@Qualifier
@Target(AnnotationTarget.PROPERTY)
annotation class InMemoryCart
