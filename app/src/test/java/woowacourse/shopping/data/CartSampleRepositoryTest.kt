package woowacourse.shopping.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.model.Product

class CartSampleRepositoryTest {

    @Test
    fun `리포지터리의 생성자에 초기화된 값을 넣고 전체 값을 받아올 수 있다`() {
        // given
        val cartSampleRepository = CartSampleRepository(
            cartProducts = mutableListOf(
                Product(
                    name = "test",
                    price = 10,
                    imageUrl = "test2"
                )
            )
        )

        // when
        val products = cartSampleRepository.getAllCartProducts()

        // then
        assert(products.size == 1)
        assertThat(products[0].name).isEqualTo("test")
        assertThat(products[0].price).isEqualTo(10)
        assertThat(products[0].imageUrl).isEqualTo("test2")
    }
}
