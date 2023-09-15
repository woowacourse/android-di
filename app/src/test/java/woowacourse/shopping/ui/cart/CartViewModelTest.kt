package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.DataBaseCartRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.util.Dummy
import woowacourse.shopping.util.getOrAwaitValue

class CartViewModelTest {

    private lateinit var vm: CartViewModel
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
    private val cartRepository: CartRepository =
        DataBaseCartRepository(FakeCartProductDao())

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        vm = CartViewModel(cartRepository)
    }

    @Test
    fun `모든 카트 상품을 가져와 카트 상품 목록을 업데이트 할 수 있다`() {
        // when
        vm.getAllCartProducts()
        // then
        val actual = vm.cartProducts.getOrAwaitValue()
        val expected = Dummy.cartProducts
        assertEquals(expected, actual)
    }

    @Test
    fun `카트 상품을 삭제하면, 카트 삭제 여부가 true 가 된다`() {
        // when
        vm.deleteCartProduct(0)
        // then
        val expected = true
        val actual = vm.onCartProductDeleted.getOrAwaitValue()
        assertEquals(expected, actual)
    }
}
