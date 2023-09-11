package woowacourse.shopping.data

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.createProduct
import woowacourse.shopping.fake.FakeCartProductDao
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.toProduct

class CartRepositoryTest {
    private lateinit var repository: CartRepository

    @Before
    fun setup() {
        repository = DatabaseCartRepository(FakeCartProductDao())
    }

    @Test
    fun `어떤 상품도 담지 않은 카트는 비어있다`() = runTest {
        // given

        // when
        val actual = repository.getAllCartProducts()

        // then
        assertThat(actual).isEmpty()
    }

    @Test
    fun `카트에 상품을 추가하면 카트에 상품이 추가된다`() = runTest {
        // given

        // when
        val product = createProduct()
        repository.addCartProduct(product)

        // then
        assertThat(repository.getAllCartProducts().map { it.toProduct() }).contains(product)
    }

    @Test
    fun `카트에 상품이 추가된 상태에서 동일한 상품을 삭제하면 카트에서 상품이 삭제된다`() = runTest {
        // given
        val product = createProduct()
        repository.addCartProduct(product)
        assertThat(repository.getAllCartProducts().map { it.toProduct() }).contains(product)

        // when
        repository.deleteCartProduct(0)

        // then
        assertThat(repository.getAllCartProducts().map { it.toProduct() }).doesNotContain(product)
    }
}
