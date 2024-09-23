package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.example.alsonglibrary2.di.AutoDIManager
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartViewModel

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var activity: CartActivity
    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        activity =
            Robolectric
                .buildActivity(CartActivity::class.java)
                .create()
                .get()

        viewModel = ViewModelProvider(activity)[CartViewModel::class.java]
    }

    @Test
    fun `Activity 실행 테스트`() {
        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `DateFormatter를 자동으로 주입할 수 있다`() {
        // then
        assertThat(activity.dateFormatter).isNotNull()
    }

    @Test
    fun `CartActivity가 destroy 되어도 CartRepository의 인스턴스는 유지된다`() {
        // given
        activity =
            Robolectric
                .buildActivity(CartActivity::class.java)
                .create()
                .destroy()
                .get()

        // then
        val actual = AutoDIManager.dependencies[CartRepository::class]
        assertThat(actual).isNotNull()
    }
}
