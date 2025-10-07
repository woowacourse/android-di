package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fixture.POTATO
import woowacourse.shopping.ui.utils.getOrAwaitValue

@RunWith(RobolectricTestRunner::class)
class CartViewModelTest {
    @get:Rule
    val instant = InstantTaskExecutorRule()
    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: CartRepository


    @Before
    fun setup() {
        cartRepository = FakeCartRepository()
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `모든 장바구니를 가져오는지 테스트`() {
        //given
        val expected = listOf(POTATO)
        //when
        viewModel.getAllCartProducts()
        //then
        assertThat(viewModel.cartProducts.getOrAwaitValue()).isEqualTo(expected)
    }

    @Test
    fun `카트에서 제품이 제거될 경우 _onCartProductDeleted가 true가 되는지 테스트`() {
        //when
        viewModel.deleteCartProduct(0)
        //then
        assertTrue(viewModel.onCartProductDeleted.getOrAwaitValue())
    }
}