package woowacourse.shopping.ui

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
class MainActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Robolectric.getForegroundThreadScheduler().pause()
    }

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `Product 클릭 시 토스트 메시지 출력 테스트`() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .start()
                .resume()
                .get()

        // when
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)
        recyclerView.measure(0, 0)
        recyclerView.layout(0, 0, 100, 100)
        recyclerView.findViewHolderForAdapterPosition(0)?.itemView?.performClick()

        // then
        val expectedMessage = activity.getString(R.string.cart_added)
        val actualMessage = ShadowToast.getTextOfLatestToast()
        assertThat(actualMessage).isEqualTo(expectedMessage)
    }
}
