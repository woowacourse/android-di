package woowacourse.shopping.ui

import androidx.appcompat.widget.Toolbar
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowActivity
import woowacourse.shopping.R
import woowacourse.shopping.ui.cart.CartActivity

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `장바구니 버튼을 누르면 장바구니 화면으로 이동한다`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()

        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar).menu

        activity.onCreateOptionsMenu(toolbar)

        val cartMenuActionView = toolbar.findItem(R.id.cart).actionView!!

        // when
        cartMenuActionView.performClick()

        // and
        val shadowActivity: ShadowActivity = Shadows.shadowOf(activity)
        val startedIntent = shadowActivity.nextStartedActivity
        assertThat(startedIntent.component?.className).isEqualTo(CartActivity::class.java.name)
    }
}
