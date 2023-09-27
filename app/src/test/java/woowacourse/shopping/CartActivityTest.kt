package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartViewModel
import kotlin.reflect.full.memberProperties

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
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()
        val viewModel =
            ViewModelProvider(
                activity,
                activity.halfMoonViewModelFactory,
            )[CartViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `DateFormatter가 주입된다`() {
        // given
        val activity: CartActivity = Robolectric
            .buildActivity(CartActivity::class.java)
            .setup()
            .get()

        // when

        // then
        assertThat(activity::class.memberProperties.any { it.name == "dateFormatter" }).isNotNull()
    }

    @Test
    fun `dateFormatter는 CartActivity가 구성변경시 새로 초기화 되지 않는다`() {
        // given: CartActivity를 인스턴스화 하고 초기화된 dateFormatter 인스턴스를 가져온다.
        val activity: ActivityController<CartActivity> = Robolectric
            .buildActivity(CartActivity::class.java)
            .setup()

        val originalDateFormatterInstance =
            activity.get()::class.java.getDeclaredField("dateFormatter")
                .apply { isAccessible = true }
                .get(activity.get())

        // when: CartActivity를 구성변경 시키면
        val recreatedActivity = activity.configurationChange()
        val recreatedDateFormatterInstance =
            recreatedActivity.get()::class.java.getDeclaredField("dateFormatter")
                .apply { isAccessible = true }
                .get(activity.get())

        // then: 기존의 dateFormatter인스턴스와 구성변경후 dateFormatter인스턴스가 같은 인스턴스이다.
        println("기존의 dateFormatter 인스턴스: $originalDateFormatterInstance")
        println("구성변경후 dateFormatter 인스턴스: $recreatedDateFormatterInstance")
        assertThat(originalDateFormatterInstance).isSameInstanceAs(recreatedDateFormatterInstance)
    }
}
