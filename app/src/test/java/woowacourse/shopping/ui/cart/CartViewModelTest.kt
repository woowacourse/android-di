package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fixture.ProductFixture
import woowacourse.shopping.repository.CartRepository

class CartViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var vm: CartViewModel
    private lateinit var cartRepository: CartRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        cartRepository = mockk()
        vm = CartViewModel(cartRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `장바구니에 담긴 모든 상품을 요청하면 불러와진다`() {
        val fakeProducts = ProductFixture.getCartProducts(listOf(1, 2, 3, 4, 5))

        // given
        coEvery { cartRepository.getAllCartProducts() } returns fakeProducts

        // when
        vm.getAllCartProducts()

        // then
        assertThat(vm.cartProducts.value).isEqualTo(fakeProducts)
    }

    @Test
    fun `상품 삭제 요청하면 상품이 삭제된다`() {
        // given
        coEvery { cartRepository.deleteCartProduct(any()) } just runs

        // when
        vm.deleteCartProduct(1L)

        // then
        assertThat(vm.onCartProductDeleted.value).isTrue
    }
}
