package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.DataBaseCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.util.Dummy
import woowacourse.shopping.util.getOrAwaitValue

class MainViewModelTest {

    private lateinit var vm: MainViewModel

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
        DataBaseCartRepository(cartProductDao = FakeCartProductDao())
    private val productRepository: ProductRepository = DefaultProductRepository()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        vm = MainViewModel(
            productRepository = productRepository,
            cartRepository = cartRepository,
        )
    }

    @Test
    fun `카트에 상품을 추가하면, 상품 추가 여부가 true 가 된다`() {
        // when
        vm.addCartProduct(Dummy.product)
        // then
        val expected = true
        val actual = vm.onProductAdded.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `전체 상품 목록을 가져와 상품 목록을 업데이트 할 수 있다`() {
        // when
        vm.getAllProducts()
        // then
        val actual = vm.products.getOrAwaitValue()
        val expected = Dummy.products
        assertEquals(expected, actual)
    }
}
