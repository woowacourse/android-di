package woowacourse.shopping.cart

import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartViewModel

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {
    private lateinit var activityController: ActivityController<CartActivity>

    @Before
    fun setup() {
        activityController = Robolectric.buildActivity(CartActivity::class.java)
    }

    @Test
    fun `Activity 실행 테스트`() {
        // when
        val activity = activityController.create().get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // when
        val activity = activityController.create().get()
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `DateFormatter 주입 테스트`() {
        // when
        val activity = activityController.create().get()
        val dateFormatter = activity.dateFormatter

        // then
        assertThat(dateFormatter).isNotNull()
    }

    @Test
    fun `DateFormatter는 화면이 회전되면 새로 생성된다`() {
        // given
        val activity = activityController.setup()
        val originalDateFormatter = activity.get().dateFormatter

        // when
        val configurationChangedActivity = activityController.recreate()
        val newDateFormatter = configurationChangedActivity.get().dateFormatter

        // then
        assertThat(originalDateFormatter).isNotEqualTo(newDateFormatter)
    }
}
