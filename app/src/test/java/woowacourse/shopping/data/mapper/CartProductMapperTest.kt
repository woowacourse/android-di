@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.data.mapper

import junit.framework.TestCase.assertEquals
import org.junit.Test
import woowacourse.shopping.data.CartProductEntity

class CartProductMapperTest {
    @Test
    fun `CartProductEntity를 CartProduct로 매핑하는 과정에서 id와 createdAt이 유지된다`() {
        val entity = CartProductEntity(
            name = "별터초코비",
            price = 600000,
            imageUrl = "주소"
        ).apply {
            id = 25L
            createdAt = 12345678L
        }
        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.createdAt, domain.createdAt)
    }
}
