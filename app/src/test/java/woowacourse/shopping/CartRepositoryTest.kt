package woowacourse.shopping

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.fixture.FakeCartProductDao
import woowacourse.fixture.PRODUCT1
import woowacourse.shopping.data.repository.DefaultCartRepository

class CartRepositoryTest {
    private lateinit var cartRepository: DefaultCartRepository

    @Before
    fun setup() {
        cartRepository =
            DefaultCartRepository(
                cartProductDao = FakeCartProductDao(),
            )
    }

    @Test
    fun `상품을 삭제할 수 있다`() =
        runTest {
            // given
            cartRepository.addCartProduct(PRODUCT1)
            val target = cartRepository.getAllCartProducts().first()
            val id = 0

            // when
            cartRepository.deleteCartProduct(id)

            // then
            val actual = cartRepository.getAllCartProducts()
            assertThat(actual).doesNotContain(target)
        }

    @Test
    fun `상품을 추가할 수 있다`() =
        runTest {
            // given
            val product = PRODUCT1

            // when
            cartRepository.addCartProduct(product)

            // then
            val actual = cartRepository.getAllCartProducts()
            assertThat(actual).anyMatch { it.id == product.id }
        }

    @Test
    fun `상품을 조회할 수 있다`() =
        runTest {
            // given
            val product = PRODUCT1
            cartRepository.addCartProduct(product)
            val expected = listOf("상품명")

            // when
            cartRepository.getAllCartProducts()

            // then
            val actual = cartRepository.getAllCartProducts().map { it.name }
            assertThat(actual).isEqualTo(expected)
        }
}
