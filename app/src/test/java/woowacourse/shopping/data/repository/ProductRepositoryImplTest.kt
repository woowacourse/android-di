package woowacourse.shopping.data.repository

import io.kotest.inspectors.forAll
import io.kotest.matchers.types.shouldBeTypeOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import woowacourse.shopping.domain.model.Product

class ProductRepositoryImplTest {
    private lateinit var repository: ProductRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository = ProductRepositoryImpl()
    }

    @DisplayName("상품 목록을 반환한다")
    @Test
    fun getAllProductsTest() {
        // given
        // when
        val products = repository.getAllProducts()

        // then
        products.forAll { product -> product.shouldBeTypeOf<Product>() }
    }
}
