package woowacourse.shopping

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowToast
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity: MainActivity =
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
        val activity: MainActivity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `상품을 클릭하면 토스트 메세지가 표시된다`() {
        // given
        val activity: MainActivity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .setup()
                .get()
        val recyclerView: RecyclerView = activity.findViewById(R.id.rv_products)
        val firstItem: View =
            recyclerView.layoutManager?.findViewByPosition(0) ?: error("상품을 찾지 못했습니다.")

        // when
        firstItem.performClick()

        // then
        val actual: String = ShadowToast.getTextOfLatestToast()
        val expected = "장바구니에 추가되었습니다."
        assertThat(actual).isEqualTo(expected)
    }
}
