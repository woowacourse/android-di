package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.repository.DefaultProductRepository

class ProductRepositoryTest {
    private lateinit var productRepository: DefaultProductRepository

    @Before
    fun setup() {
        productRepository = DefaultProductRepository()
    }

    @Test
    fun `모든 상품을 불러올 수 있다`() {
        // given
        val expected =
            listOf(
                "우테코 과자",
                "우테코 쥬스",
                "우테코 아이스크림",
            )

        // when
        val actual =
            productRepository.getAllProducts().map {
                it.name
            }

        // then
        assertThat(actual).isEqualTo(expected)
    }
}
