package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var activity: CartActivity

    @Before
    fun setup() {
        activity =
            Robolectric
                .buildActivity(CartActivity::class.java)
                .setup()
                .get()
    }

    @Test
    fun `Activity 실행 테스트`() {
        // then
        Truth.assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        Truth.assertThat(viewModel).isNotNull()
    }
}
