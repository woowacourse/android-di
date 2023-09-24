package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()

        // then
        Truth.assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given

        // when
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()

        // when
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        Truth.assertThat(viewModel).isNotNull()
    }

    @Test
    fun `onCreate상태가 되면 dateFormatter는 instanceContainer에 존재한다`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .setup()
            .get()

        // when
        val actual = activity.instanceContainer.value?.any {
            it.clazz.contains(DateFormatter::class)
        }

        // then
        Truth.assertThat(actual).isNotNull()
    }

    @Test
    fun `onDestroy상태가 되면 dateFormatter는 삭제된다`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .destroy()
            .get()

        // when
        val actual = activity.instanceContainer.value?.find {
            it.clazz.any { clazz ->
                clazz == DateFormatter::class
            }
        }

        // then
        Truth.assertThat(actual).isNull()
    }
}
