package woowacourse.shopping

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class CartRepositoryTest {
    @Test
    fun `상품을 도메인 모델로 변환하고 실제 식별자로 삭제한다`() =
        runTest {
            val repository = RoomCartRepository(FakeCartProductDao())
            val firstProduct = Product(name = "첫 상품", price = 1_000, imageUrl = "first")
            val secondProduct = Product(name = "둘째 상품", price = 2_000, imageUrl = "second")

            repository.addCartProduct(firstProduct)
            repository.addCartProduct(secondProduct)
            val products = repository.getAllCartProducts()
            repository.deleteCartProduct(products.last().id)

            assertThat(products)
                .containsExactly(
                    CartProduct(1L, "첫 상품", 1_000, "first", FIXED_CREATED_AT),
                    CartProduct(2L, "둘째 상품", 2_000, "second", FIXED_CREATED_AT),
                )
            assertThat(repository.getAllCartProducts())
                .containsExactly(CartProduct(1L, "첫 상품", 1_000, "first", FIXED_CREATED_AT))
        }

    private companion object {
        const val FIXED_CREATED_AT = 1_700_000_000_000L
    }
}

private class FakeCartProductDao : CartProductDao {
    private val cartProducts = mutableListOf<CartProductEntity>()
    private var nextId = 1L

    override suspend fun getAll(): List<CartProductEntity> = cartProducts.toList()

    override suspend fun insert(cartProduct: CartProductEntity) {
        cartProduct.id = nextId++
        cartProduct.createdAt = FIXED_CREATED_AT
        cartProducts.add(cartProduct)
    }

    override suspend fun delete(id: Long) {
        cartProducts.removeAll { it.id == id }
    }

    private companion object {
        const val FIXED_CREATED_AT = 1_700_000_000_000L
    }
}
