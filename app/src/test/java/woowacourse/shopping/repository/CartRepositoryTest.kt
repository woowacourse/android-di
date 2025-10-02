package woowacourse.shopping.repository

import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.domain.Product

class CartRepositoryTest {
    private lateinit var cartRepository: CartRepositoryImpl

    @Before
    fun setUp() {
        cartRepository = CartRepositoryImpl()
    }

    @Test
    fun `addCartProduct_호출_시_장바구니에_상품이_추가된다`() {
        // Given
        val product = Product("상품1", 1000, "")

        // When
        cartRepository.addCartProduct(product)

        // Then
        val cartProducts = cartRepository.getAllCartProducts()
        Assertions.assertThat(cartProducts).containsExactly(product)
        Assertions.assertThat(cartProducts.size).isEqualTo(1)
    }

    @Test
    fun `getAllCartProducts_호출_시_모든_장바구니_상품이_호출된다`() {
        // Given
        val product1 = Product("상품1", 1000, "")
        val product2 = Product("상품2", 2000, "")

        // When
        cartRepository.addCartProduct(product1)
        cartRepository.addCartProduct(product2)

        // Then
        val cartProducts = cartRepository.getAllCartProducts()
        Assertions.assertThat(cartProducts.size).isEqualTo(2)
    }

    @Test
    fun `deleteCartProduct_호출_시_해당_상품이_삭제된다`() {
        // Given
        val product1 = Product("상품1", 1000, "")
        val product2 = Product("상품2", 2000, "")
        cartRepository.addCartProduct(product1)
        cartRepository.addCartProduct(product2)

        // When
        cartRepository.deleteCartProduct(0)

        // Then
        val cartProducts = cartRepository.getAllCartProducts()
        Assertions.assertThat(cartProducts).containsExactly(product2)
        Assertions.assertThat(cartProducts.size).isEqualTo(1)
    }
}
