package woowacourse.shopping.data

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.util.Dummy

@OptIn(ExperimentalCoroutinesApi::class)
class CartRepositoryImplTest {
    private lateinit var cartRepository: CartRepository

    class FakeCartProductDao : CartProductDao {
        private val cartProducts = Dummy.cartProducts.toMutableList()
        override suspend fun getAll(): List<CartProductEntity> {
            return cartProducts.toList()
        }

        override suspend fun insert(cartProduct: CartProductEntity) {
            cartProducts.add(cartProduct)
        }

        override suspend fun delete(id: Long) {
            cartProducts.removeIf { it.id == id }
        }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        cartRepository = CartRepositoryImpl(
            FakeCartProductDao()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `모든 카트 상품들을 반환한다`() = runTest {
        val actual = cartRepository.getAllCartProducts()
        // then
        val expected = Dummy.products
        assertEquals(expected, actual)
    }

    @Test
    fun `카트 상품을 지울 수 있다`() = runTest {
        // given
        val originalCartProducts = cartRepository.getAllCartProducts()
        // when
        cartRepository.deleteCartProduct(0)
        // then
        val actual = cartRepository.getAllCartProducts().contains(originalCartProducts[0])
        val expected = false
        assertEquals(expected, actual)
    }

    @Test
    fun `카트 상품을 추가할 수 있다`() = runTest {
        // given
        val originalCartProducts = cartRepository.getAllCartProducts()
        // when
        cartRepository.addCartProduct(product = Dummy.product)
        // then
        val actual = cartRepository.getAllCartProducts()
        val expected = originalCartProducts + Dummy.product
        assertEquals(expected, actual)
    }
}
