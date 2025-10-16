package woowacourse.shopping.repository

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll
import woowacourse.shopping.domain.Product
import woowacourse.shopping.domain.toData
import woowacourse.shopping.fixture.FakeCartRepository

class CartRepositoryTest {
    private lateinit var cartRepository: FakeCartRepository

    @Before
    fun setUp() {
        cartRepository = FakeCartRepository()
    }

    @Test
    fun `addCartProduct_호출_시_장바구니에_상품이_추가된다`() =
        runTest {
            // Given
            val product = Product("상품1", 1000, "")
            val cartItem = product.toData()

            // When
            cartRepository.addCartProduct(cartItem)

            // Then
            val cartProducts = cartRepository.getAllCartProducts()
            assertAll(
                { assertThat(cartProducts).containsExactly(cartItem) },
                { assertThat(cartProducts).hasSize(1) },
            )
        }

    @Test
    fun `getAllCartProducts_호출_시_모든_장바구니_상품이_호출된다`() =
        runTest {
            // Given
            val product1 = Product("상품1", 1000, "")
            val product2 = Product("상품2", 2000, "")

            // When
            cartRepository.addCartProduct(product1.toData())
            cartRepository.addCartProduct(product2.toData())

            // Then
            val cartProducts = cartRepository.getAllCartProducts()
            Assertions.assertThat(cartProducts.size).isEqualTo(2)
        }

    @Test
    fun `deleteCartProduct_호출_시_해당_상품이_삭제된다`() =
        runTest {
            // Given
            val product1 = Product("상품1", 1000, "")
            val product2 = Product("상품2", 2000, "")
            val cartItem1 = product1.toData()
            val cartItem2 = product2.toData()
            cartRepository.addCartProduct(cartItem1)
            cartRepository.addCartProduct(cartItem2)

            // When
            cartRepository.deleteCartProduct(1L)

            // Then
            val cartProducts = cartRepository.getAllCartProducts()
            assertAll(
                { assertThat(cartProducts).containsExactly(cartItem1) },
                { assertThat(cartProducts).hasSize(1) },
            )
        }
}
