package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartViewModel

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var activity: CartActivity
    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        activity =
            Robolectric
                .buildActivity(CartActivity::class.java)
                .create()
                .get()

        viewModel = ViewModelProvider(activity)[CartViewModel::class.java]
    }

    @Test
    fun `Activity 실행 테스트`() {
        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `카트 상품이 가져와져야 한다`() {
        // when
        viewModel.getAllCartProducts()

        // then
        assertThat(viewModel.cartProducts.getOrAwaitValue()[0].name).isEqualTo("우테코 과자")
        assertThat(viewModel.cartProducts.getOrAwaitValue()[1].name).isEqualTo("우테코 쥬스")
        assertThat(viewModel.cartProducts.getOrAwaitValue()[2].name).isEqualTo("우테코 아이스크림")
    }
}
