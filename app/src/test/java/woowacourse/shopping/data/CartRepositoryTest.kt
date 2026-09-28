package woowacourse.shopping.data

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CartRepositoryTest {
    class FakeCartProductDao(
        private val cartProducts: List<CartProductEntity>,
    ) : CartProductDao {
        override suspend fun getAll(): List<CartProductEntity> = cartProducts

        override suspend fun insert(cartProduct: CartProductEntity) {
        }

        override suspend fun delete(id: Long) {
            TODO("Not yet implemented")
        }
    }

    @Test
    fun `장바구니 상품을 도메인 모델로 조회한다`() =
        runTest {
            val entities = createCartProductEntities()
            val repository = DefaultCartRepository(FakeCartProductDao(entities))

            val cartProducts = repository.getAllCartProducts()

            assertThat(cartProducts).hasSize(entities.size)
            cartProducts.zip(entities).forEach { (cartProduct, entity) ->
                assertThat(cartProduct.id).isEqualTo(entity.id)
                assertThat(cartProduct.name).isEqualTo(entity.name)
                assertThat(cartProduct.price).isEqualTo(entity.price)
                assertThat(cartProduct.imageUrl).isEqualTo(entity.imageUrl)
                assertThat(cartProduct.createdAt).isEqualTo(entity.createdAt)
            }
        }

    private fun createCartProductEntities(): List<CartProductEntity> =
        listOf(
            CartProductEntity(
                name = "우테코 과자",
                price = 10_000,
                imageUrl = "https://example.com/snack.png",
            ).apply {
                id = 1L
                createdAt = 1_700_000_000_000L
            },
            CartProductEntity(
                name = "우테코 음료",
                price = 2_000,
                imageUrl = "https://example.com/drink.png",
            ).apply {
                id = 2L
                createdAt = 1_800_000_000_000L
            },
        )
}
