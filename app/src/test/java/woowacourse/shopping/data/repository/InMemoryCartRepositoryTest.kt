package woowacourse.shopping.data.repository

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import woowacourse.shopping.DEFAULT_CART_PRODUCT
import woowacourse.shopping.domain.repository.CartRepository

class InMemoryCartRepositoryTest {
    private lateinit var repository: CartRepository

    @BeforeEach
    fun setUp() {
        repository = InMemoryCartRepository()
    }

    @DisplayName("장바구니에 상품을 추가하고 반환한다")
    @Test
    fun addAndGetAllCartProductsTest() =
        runTest {
            // given
            // when
            repository.addCartProduct(DEFAULT_CART_PRODUCT)
            val products = repository.getAllCartProducts()

            // then
            products.shouldContain(DEFAULT_CART_PRODUCT)
        }

    @DisplayName("장바구니에서 상품을 제거한다")
    @Test
    fun deleteCartProductTest() =
        runTest {
            // given
            repository.addCartProduct(DEFAULT_CART_PRODUCT)

            // when
            DEFAULT_CART_PRODUCT.id?.let { repository.deleteCartProduct(it) }

            // then
            repository.getAllCartProducts().shouldNotContain(DEFAULT_CART_PRODUCT)
        }
}
