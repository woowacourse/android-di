package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.ui.cart.CartViewModel

class CartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    lateinit var viewModel: CartViewModel

    @Before
    fun setup() {
        // given
        viewModel = CartViewModel()
    }
//
//    @Test
//    fun `카트에_담긴_상품을_가져올_수_있다`() {
//        // when
//        viewModel.getAllCartProducts()
//        // then
//        assertThat(viewModel.cartProducts.value).isEqualTo(fakeProducts)
//    }
//
//    @Test
//    fun `카트에_담긴_상품을_삭제할_수_있다`() =
//        runTest {
//            // when
//            viewModel.deleteCartProduct(0)
//            // then
//            assertThat().isEqualTo(fakeCartProducts.size - 1)
//        }
}
