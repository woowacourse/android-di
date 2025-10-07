package woowacourse.shopping.domain.model

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProductTest {
    @DisplayName("상품이 생성된다")
    @Test
    fun productTest() {
        // given
        // when
        val product = Product(name = "name", price = 1000, imageUrl = "imageUrl")

        // then
        assertSoftly(product) {
            name shouldBe "name"
            price shouldBe 1000
            imageUrl shouldBe "imageUrl"
        }
    }
}
