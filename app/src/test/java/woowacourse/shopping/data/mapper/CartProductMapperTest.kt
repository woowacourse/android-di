package woowacourse.shopping.data.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.data.CartProductEntity

class CartProductMapperTest {
    @Test
    fun `장바구니 엔티티를 도메인 모델로 변환한다`() {
        val entity =
            CartProductEntity(
                name = "우테코 과자",
                price = 10_000,
                imageUrl = "https://example.com/",
            ).apply {
                id = 1L
                createdAt = 1_700_000_000_000L
            }

        val cartProduct = entity.toDomain()

        assertThat(cartProduct.id).isEqualTo(entity.id)
        assertThat(cartProduct.name).isEqualTo(entity.name)
        assertThat(cartProduct.price).isEqualTo(entity.price)
        assertThat(cartProduct.imageUrl).isEqualTo(entity.imageUrl)
        assertThat(cartProduct.createdAt).isEqualTo(entity.createdAt)
    }
}
