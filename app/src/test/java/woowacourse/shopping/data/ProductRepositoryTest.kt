package woowacourse.shopping.data

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ProductRepositoryTest {
    @Test
    fun `상품_조회시_상품이_순서대로_조회된다`() {
        // given
        val repository: ProductRepository = ProductRepositoryImpl()

        // when
        val products = repository.getAllProducts()

        // then
        assertThat(products).hasSize(3)
        assertThat(products.map { it.name })
            .containsAtLeast("우테코 과자", "우테코 쥬스", "우테코 아이스크림")
    }
}
