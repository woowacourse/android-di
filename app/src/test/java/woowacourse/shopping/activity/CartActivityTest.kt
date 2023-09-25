package woowacourse.shopping.activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.hyemdooly.androiddi.util.viewModels
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.activity.viewmodel.InMemoryCartViewModel
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartViewModel

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule() // AAC 컴포넌트들을 한 스레드에서 실행되도록 함

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `CartViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()
        val viewModel = activity.viewModels<CartViewModel>()

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `InMemoryCartViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()
        val viewModel = activity.viewModels<InMemoryCartViewModel>()

        // then
        assertThat(viewModel).isNotNull()
    }
}
