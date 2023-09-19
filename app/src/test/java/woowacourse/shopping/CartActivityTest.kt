package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.launchActivity
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.cart.DateFormatter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

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
        assertNotNull(activity)
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
        assertNotNull(viewModel)
    }

    @Test
    fun `Activity를 재생성했을 때, 액티비티 lifecycle을 따르는 필드 객체에 새로운 인스턴스가 주입된다`() {
        // GIVEN
        val scenario = launchActivity<CartActivity>()
        var firstDateFormatter: DateFormatter? = null
        var secondDateFormatter: DateFormatter? = null

        // WHEN
        scenario.moveToState(Lifecycle.State.CREATED)

        // THEN
        scenario.onActivity { activity ->
            firstDateFormatter =
                activity::class.declaredMemberProperties.first { it.name == "dateFormatter" }
                    .apply { this.isAccessible = true }.getter.call(activity) as DateFormatter
            assertNotNull(firstDateFormatter)
        }

        // WHEN
        scenario.recreate()

        // THEN
        scenario.onActivity { activity ->
            secondDateFormatter =
                activity::class.declaredMemberProperties.first { it.name == "dateFormatter" }
                    .apply { this.isAccessible = true }.getter.call(activity) as DateFormatter
            assertNotNull(secondDateFormatter)
        }

        assert(firstDateFormatter !== secondDateFormatter)
    }
}
