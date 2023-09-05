package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.ProductRepository

class ProductRepositoryTest {
    private lateinit var repository: ProductRepository

    @Before
    fun setup() {
        repository = DefaultProductRepository()
    }

    @Test
    fun `모든 상품을 조회하면 모든 상품을 가져올 수 있다`() {
        // given
        val products = listOf(
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

        // when
        val actual = repository.getAllProducts()

        // then
        assertThat(actual).isEqualTo(products)
    }
}
