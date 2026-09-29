@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.data

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.model.Product

class InMemoryCartRepositoryTest {
    private val repository = InMemoryCartRepository()

    @Test
    fun `상품을 추가하면 장바구니에서 조회할 수 있다`() =
        runTest {
            repository.addCartProduct(
                Product(
                    name = "사과",
                    price = 1_000,
                    imageUrl = "apple.png",
                ),
            )

            val cartProducts = repository.getAllCartProducts()

            assertThat(cartProducts).hasSize(1)
            assertThat(cartProducts[0].id).isEqualTo(1L)
            assertThat(cartProducts[0].name).isEqualTo("사과")
            assertThat(cartProducts[0].price).isEqualTo(1_000)
            assertThat(cartProducts[0].imageUrl).isEqualTo("apple.png")
            assertThat(cartProducts[0].createdAt).isGreaterThan(0L)
        }

    @Test
    fun `상품을 실제 id로 삭제할 수 있다`() =
        runTest {
            repository.addCartProduct(
                Product("사과", 1_000, "apple.png"),
            )
            repository.addCartProduct(
                Product("바나나", 2_000, "banana.png"),
            )
            repository.addCartProduct(
                Product("포도", 3_000, "grape.png"),
            )

            val cartProducts = repository.getAllCartProducts()
            val middleProductId = cartProducts[1].id

            repository.deleteCartProduct(middleProductId)

            val remainingProducts = repository.getAllCartProducts()

            assertThat(remainingProducts)
                .extracting<String> { it.name }
                .containsExactly("사과", "포도")

            assertThat(remainingProducts)
                .extracting<Long> { it.id }
                .containsExactly(1L, 3L)
        }
}
