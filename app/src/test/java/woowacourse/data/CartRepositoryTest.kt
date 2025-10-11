package woowacourse.data

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import woowacourse.fake.FakeCartProductDao
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.domain.model.Product

class CartRepositoryTest {
    private val cartDao = FakeCartProductDao()
    private lateinit var repository: DefaultCartRepository

    @Before
    fun setUp() {
        repository = DefaultCartRepository(cartDao)
    }

    @Test
    fun `상품을 장바구니에 추가하면 목록에 추가되고 장바구니 상품을 가져온다`() =
        runTest {
            // given
            val product =
                Product(
                    id = 0,
                    name = "사과",
                    price = 3000,
                    imageUrl = "",
                    createdAt = System.currentTimeMillis(),
                )

            // when
            repository.addCartProduct(product)

            // then
            val cartItems = repository.getAllCartProducts()
            assertTrue(cartItems.any { it.id == product.id })
        }

    @Test
    fun `상품을 삭제하면 장바구니에서 제거된다`() =
        runTest {
            // given
            val product =
                Product(
                    id = 0,
                    name = "사과",
                    price = 3000,
                    imageUrl = "",
                    createdAt = System.currentTimeMillis(),
                )
            repository.addCartProduct(product)

            // when
            repository.deleteCartProduct(0)

            // then
            val cartItems = repository.getAllCartProducts()
            assertTrue(cartItems.isEmpty())
        }
}
