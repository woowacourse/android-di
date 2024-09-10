package woowacourse.shopping.ui.cart

import android.widget.ImageView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowToast
import woowacourse.shopping.R

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Robolectric.getForegroundThreadScheduler().pause()
    }

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
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }
}
