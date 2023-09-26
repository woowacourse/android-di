package woowacourse.shopping.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartViewModel

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
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        Truth.assertThat(viewModel).isNotNull()
    }

    @Test
    fun `acitivity의 container가 recreate되면 새로운 컨테이너가 생성된다`() {
        // given
        val activityController = Robolectric.buildActivity(CartActivity::class.java)
        val activity = activityController.create().get()

        // when
        val origin = activity.container
        activityController.recreate()
        val new = activity.container

        // then
        TestCase.assertNotSame(origin, new)
    }

    @Test
    fun `acitivity의 container가 Destroy된 후 새로운 activity가 생성되면 새 컨테이너가 생성된다`() {
        // given
        val activityController = Robolectric.buildActivity(CartActivity::class.java)
        val activity = activityController.create().get()

        // when
        val origin = activity.container
        activityController.pause().stop().destroy()
        val new = activity.container

        // then
        TestCase.assertNotSame(origin, new)
    }

    @Test
    fun `acitivity의 container가 구성 변경 되어도 새로운 컨테이너가 생성되지 않는다`() {
        // given
        val activityController = Robolectric.buildActivity(CartActivity::class.java)
        val activity = activityController.create().get()

        // when
        val origin = activity.container
        activityController.configurationChange()
        val new = activity.container

        // then
        Assert.assertSame(origin, new)
    }
}
