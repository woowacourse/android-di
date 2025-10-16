package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartActivity

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity =
            Robolectric
                .buildActivity(CartActivity::class.java)
                .create()
                .get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val viewModel = DependencyInjection.inject(MainViewModel::class.java)

        // then
        assertThat(viewModel).isNotNull()
    }
}
