package woowacourse.shopping.data

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.model.Product

class CartInMemoryRepositoryTest {

    @Test
    fun `리포지터리의 생성자에 초기화된 값을 넣고 전체 값을 받아올 수 있다`() = runTest {
        // given
        val cartInMemoryRepository = CartInMemoryRepository(
            cartProducts = mutableListOf(
                Product(
                    name = "test",
                    price = 10,
                    imageUrl = "test2",
                ),
            ),
        )

        // when
        val products = cartInMemoryRepository.getAllCartProducts()

        // then
        assert(products.size == 1)
        assertThat(products[0].name).isEqualTo("test")
        assertThat(products[0].price).isEqualTo(10)
        assertThat(products[0].imageUrl).isEqualTo("test2")
    }

    @Test
    fun `리포지터리에 값을 넣고 값을 받아올 수 있다`() = runTest {
        // given
        val cartInMemoryRepository = CartInMemoryRepository()
        val product = Product(
            name = "test",
            price = 10,
            imageUrl = "test2",
        )

        // when
        cartInMemoryRepository.addCartProduct(product)
        val products = cartInMemoryRepository.getAllCartProducts()

        // then
        assert(products.size == 1)
        assertThat(products[0].name).isEqualTo("test")
        assertThat(products[0].price).isEqualTo(10)
        assertThat(products[0].imageUrl).isEqualTo("test2")
    }

    @Test
    fun `리포지터리의 값을 삭제할 수 있다`() = runTest {
        // given
        val cartInMemoryRepository = CartInMemoryRepository()
        val product = Product(
            name = "test",
            price = 10,
            imageUrl = "test2",
        )

        // when
        cartInMemoryRepository.addCartProduct(product)
        cartInMemoryRepository.deleteCartProduct(0)
        val products = cartInMemoryRepository.getAllCartProducts()

        // then
        assert(products.isEmpty())
    }
}
