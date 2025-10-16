package woowacourse.shopping.data

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import woowacourse.shopping.fixture.PRODUCT_3
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class DatabaseCartRepositoryTest {
    @Test
    fun `장바구니에 상품을 추가할 수 있다`() =
        runTest {
            // given
            val repository = DatabaseCartRepository(FakeCartProductDao())
            val initialProducts: List<Product> =
                repository.getAllCartProducts().map(CartProduct::product)
            assertThat(initialProducts).doesNotContain(PRODUCT_3)

            // when
            repository.addCartProduct(PRODUCT_3)

            // then
            val finalProducts: List<Product> =
                repository.getAllCartProducts().map(CartProduct::product)
            assertThat(finalProducts).contains(PRODUCT_3)
        }

    @Test
    fun `장바구니에서 특정 ID를 가진 상품을 제거할 수 있다`() =
        runTest {
            // given
            val repository = DatabaseCartRepository(FakeCartProductDao())
            val initialIds: List<Long> = repository.getAllCartProducts().map(CartProduct::id)
            assertThat(initialIds).contains(1L)

            // when
            repository.deleteCartProduct(1L)

            // then
            val finalIds: List<Long> = repository.getAllCartProducts().map(CartProduct::id)
            assertThat(finalIds).doesNotContain(1L)
        }
}
