package woowacourse.shopping.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.Product

class ProductRepositoryTest {
    private lateinit var productRepository: DefaultProductRepository

    @Before
    fun setUp() {
        productRepository = DefaultProductRepository()
    }

    @Test
    fun `getAllProducts_호출_시_모든_상품이_반환된다`() {
        // Given
        val expectedProducts =
            listOf(
                Product(
                    name = "우테코 과자",
                    price = 10_000,
                    imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
                ),
                Product(
                    name = "우테코 쥬스",
                    price = 8_000,
                    imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700",
                ),
                Product(
                    name = "우테코 아이스크림",
                    price = 20_000,
                    imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/e703c53e-5d01-4b20-bd33-85b5e778e73f.jpg?h=700&w=700",
                ),
            )

        // When
        val products = productRepository.getAllProducts()

        // Then
        assertAll(
            { assertThat(products).isEqualTo(expectedProducts) },
            { assertThat(products.size).isEqualTo(3) },
        )
    }
}
