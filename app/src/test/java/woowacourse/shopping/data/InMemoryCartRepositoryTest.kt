package woowacourse.shopping.data

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.model.Product

class InMemoryCartRepositoryTest {
    @Test
    fun `상품을 추가하면 장바구니에서 조회할 수 있다`() =
        runTest {
            val currentTime = 1_700_000_000_000L
            val repository =
                InMemoryCartRepository(
                    currentTimeMillis = { currentTime },
                )
            val product =
                Product(
                    name = "우테코 과자",
                    price = 10_000,
                    imageUrl = "https://example.com/snack.png",
                )

            repository.addCartProduct(product)

            val cartProduct = repository.getAllCartProducts().single()
            assertThat(cartProduct.id).isEqualTo(1L)
            assertThat(cartProduct.name).isEqualTo(product.name)
            assertThat(cartProduct.price).isEqualTo(product.price)
            assertThat(cartProduct.imageUrl).isEqualTo(product.imageUrl)
            assertThat(cartProduct.createdAt).isEqualTo(currentTime)
        }

    @Test
    fun `두 상품을 추가하면 서로 다른 ID가 부여된다`() =
        runTest {
            val currentTime = 1_700_000_000_000L
            val repository =
                InMemoryCartRepository(
                    currentTimeMillis = { currentTime },
                )
            val product1 =
                Product(
                    name = "우테코 과자",
                    price = 10_000,
                    imageUrl = "https://example.com/snack.png",
                )

            val product2 =
                Product(
                    name = "우테코 과자2",
                    price = 20_000,
                    imageUrl = "https://example.com/snack.png",
                )

            repository.addCartProduct(product1)
            repository.addCartProduct(product2)

            val cartProduct1 = repository.getAllCartProducts()[0]
            val cartProduct2 = repository.getAllCartProducts()[1]

            assertThat(cartProduct1.id).isNotEqualTo(cartProduct2.id)
        }

    @Test
    fun `ID로 삭제하면 해당 상품만 제거된다`() =
        runTest {
            val currentTime = 1_700_000_000_000L
            val repository =
                InMemoryCartRepository(
                    currentTimeMillis = { currentTime },
                )
            val product =
                Product(
                    name = "우테코 과자",
                    price = 10_000,
                    imageUrl = "https://example.com/snack.png",
                )
            repository.addCartProduct(product)
            val cartProduct = repository.getAllCartProducts()
            assertThat(cartProduct.size).isEqualTo(1)

            repository.deleteCartProduct(cartProduct[0].id)
            val deleteCartProduct = repository.getAllCartProducts()
            assertThat(deleteCartProduct.size).isEqualTo(0)
        }
}
