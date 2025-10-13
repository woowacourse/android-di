package woowacourse.shopping.data

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.fixture.ProductFixture
import woowacourse.shopping.fixture.ProductsFixture

class InMemoryCartRepositoryTest {
    private lateinit var cartRepository: InMemoryCartRepository

    @Before
    fun setUp() {
        cartRepository = InMemoryCartRepository()
    }

    @Test
    fun `장바구니에 단일 상품 추가`() =
        runTest {
            cartRepository.addCartProduct(ProductFixture)

            // then
            assertThat(cartRepository.getAllCartProducts()).containsExactly(ProductFixture)
        }

    @Test
    fun `상품 삭제`() =
        runTest {
            // given
            cartRepository.addCartProduct(ProductsFixture[0])
            cartRepository.addCartProduct(ProductsFixture[1])

            // when
            cartRepository.deleteCartProduct(1)

            // then
            assertThat(cartRepository.getAllCartProducts()).doesNotContain(ProductsFixture[0])
        }

    @Test(expected = IndexOutOfBoundsException::class)
    fun `존재하지 않는 인덱스 삭제 시 예외 발생`() =
        runTest {
            // given
            cartRepository.addCartProduct(ProductFixture)

            // when & then
            cartRepository.deleteCartProduct(5)
        }

    @Test
    fun `추가와 삭제를 반복하여 일관성 유지`() =
        runTest {
            // given
            // when
            cartRepository.addCartProduct(ProductsFixture[0])
            cartRepository.addCartProduct(ProductsFixture[1])
            cartRepository.deleteCartProduct(1)
            cartRepository.addCartProduct(ProductsFixture[2])

            // then
            assertThat(cartRepository.getAllCartProducts())
                .containsExactly(
                    ProductsFixture[1],
                    ProductsFixture[2],
                )
        }
}
