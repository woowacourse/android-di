package woowacourse.shopping.data

import com.harodi.Qualifier
import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<CartProduct>

    suspend fun deleteCartProduct(id: Long)
}

class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = dao.getAll().map { it.toDomain() }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}

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
annotation class DefaultCart

@Qualifier
@Target(AnnotationTarget.PROPERTY)
annotation class InMemoryCart
