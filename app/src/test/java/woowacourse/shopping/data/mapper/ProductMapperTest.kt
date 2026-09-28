package woowacourse.shopping.data.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class ProductMapperTest {
    @Test
    fun `상품을 새 장바구니 엔티티로 변환한다`() {
        val product = Product(name = "과자", price = 10_000, imageUrl = "snack.jpg")

        val entity = product.toEntity()

        assertThat(entity.name).isEqualTo(product.name)
        assertThat(entity.price).isEqualTo(product.price)
        assertThat(entity.imageUrl).isEqualTo(product.imageUrl)
        assertThat(entity.id).isZero()
    }

    @Test
    fun `엔티티의 식별자와 담은 시각을 도메인에 보존한다`() {
        val entity =
            CartProductEntity(name = "과자", price = 10_000, imageUrl = "snack.jpg").apply {
                id = 3_000_000_000L
                createdAt = 1_700_000_000_000L
            }

        assertThat(entity.toDomain()).isEqualTo(
            CartProduct(
                id = entity.id,
                name = entity.name,
                price = entity.price,
                imageUrl = entity.imageUrl,
                createdAt = entity.createdAt,
            ),
        )
    }
}
