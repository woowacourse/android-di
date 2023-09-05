package woowacourse.shopping.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.model.Product

class ProductSampleRepositoryTest {

    @Test
    fun `리포지터리의 생성자에 초기화된 값을 넣고 값을 받아올 수 있다`() {
        // given
        val productSampleRepository = ProductSampleRepository(
            products = listOf(
                Product(
                    name = "test",
                    price = 10,
                    imageUrl = "test2"
                )
            )
        )

        // when
        val products = productSampleRepository.getAllProducts()

        // then
        assert(products.size == 1)
        assertThat(products[0].name).isEqualTo("test")
        assertThat(products[0].price).isEqualTo(10)
        assertThat(products[0].imageUrl).isEqualTo("test2")
    }
}
