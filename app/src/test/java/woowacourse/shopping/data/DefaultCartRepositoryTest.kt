package woowacourse.shopping.data

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.fixture.productsFixture

class DefaultCartRepositoryTest {
    private lateinit var repository: DefaultCartRepository

    @Before
    fun setup() {
        repository = DefaultCartRepository()
    }

    @Test
    fun `장바구니에 상품을 추가할 수 있다`() {
        // Given
        repository.addCartProduct(productsFixture[0])

        // Then
        val cartProducts = repository.getAllCartProducts()
        assertThat(cartProducts).isEqualTo(listOf(productsFixture[0]))
    }

    @Test
    fun `장바구니에 상품을 삭제할 수 있다`() {
        // Given
        repository.addCartProduct(productsFixture[0])
        repository.addCartProduct(productsFixture[1])
        repository.deleteCartProduct(0)

        // Then
        val cartProducts = repository.getAllCartProducts()
        assertThat(cartProducts).isEqualTo(listOf(productsFixture[1]))
    }

    @Test
    fun `장바구니에 상품을 조회할 수 있다`() {
        // Given
        repository.addCartProduct(productsFixture[0])
        repository.addCartProduct(productsFixture[1])

        // Then
        val cartProducts = repository.getAllCartProducts()
        assertThat(cartProducts).containsExactly(productsFixture[0], productsFixture[1])
    }
}
