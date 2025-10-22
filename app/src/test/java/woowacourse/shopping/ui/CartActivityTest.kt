package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `액티비티를_recreate하면_DateFormatter가_재생성된다`() {
        // given
        val controller: ActivityController<CartActivity> =
            Robolectric.buildActivity(CartActivity::class.java).setup()
        val firstFormatter: DateFormatter = extractDateFormatter(controller.get())

        // when
        controller.recreate()
        val recreatedFormatter: DateFormatter = extractDateFormatter(controller.get())

        // then
        assertThat(firstFormatter).isNotSameInstanceAs(recreatedFormatter)
    }

    private fun extractDateFormatter(activity: CartActivity): DateFormatter {
        val field = CartActivity::class.java.getDeclaredField("dateFormatter")
        field.isAccessible = true
        return field.get(activity) as DateFormatter
    }
}
