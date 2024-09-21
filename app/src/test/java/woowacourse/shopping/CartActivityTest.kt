package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import com.woowa.di.test.DIActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartViewModel

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {
    private lateinit var activity: CartActivity

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val diRule = DIActivityTestRule(CartActivity::class.java)

    @Before
    fun setUp() {
        activity = diRule.getActivity()
    }

    @Test
    fun `Activity 실행 테스트`() {
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }
}
