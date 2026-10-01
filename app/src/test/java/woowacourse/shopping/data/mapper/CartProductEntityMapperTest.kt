package woowacourse.shopping.data.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.data.CartProductEntity

class CartProductEntityMapperTest {
    @Test
    fun `장바구니 상품의 DB ID와 담은 시각을 도메인 객체에 전달한다`() {
        val entity =
            CartProductEntity(
                name = "우테코 과자",
                price = 10_000,
                imageUrl = "image-url",
            ).apply {
                id = 42L
                createdAt = 1_700_000_000_000L
            }

        val product = entity.toDomain()

        assertThat(product.id).isEqualTo(entity.id)
        assertThat(product.createdAt).isEqualTo(entity.createdAt)
        assertThat(product.name).isEqualTo(entity.name)
        assertThat(product.price).isEqualTo(entity.price)
        assertThat(product.imageUrl).isEqualTo(entity.imageUrl)
    }
}
