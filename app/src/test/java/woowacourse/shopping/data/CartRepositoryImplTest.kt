package woowacourse.shopping.data

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import woowacourse.shopping.DEFAULT_PRODUCT
import woowacourse.shopping.domain.repository.CartRepository

class CartRepositoryImplTest {
    private lateinit var repository: CartRepository

    @BeforeEach
    fun setUp() {
        repository = CartRepositoryImpl()
    }

    @DisplayName("장바구니에 상품을 추가하고 반환한다")
    @Test
    fun addAndGetAllCartProductsTest() {
        // given
        // when
        repository.addCartProduct(DEFAULT_PRODUCT)
        val products = repository.getAllCartProducts()

        // then
        assertSoftly(products) {
            size shouldBe 1
            shouldContain(DEFAULT_PRODUCT)
        }
    }

    @DisplayName("장바구니에서 상품을 제거한다")
    @Test
    fun deleteCartProductTest() {
        // given
        repository.addCartProduct(DEFAULT_PRODUCT)

        // when
        val products = repository.getAllCartProducts()
        val index = products.indexOf(DEFAULT_PRODUCT)
        repository.deleteCartProduct(index)

        // then
        repository.getAllCartProducts().shouldNotContain(DEFAULT_PRODUCT)
    }
}
