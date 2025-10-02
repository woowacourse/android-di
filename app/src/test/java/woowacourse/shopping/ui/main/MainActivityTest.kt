package woowacourse.shopping.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.common.truth.Truth
import org.assertj.core.api.Assertions.assertThat
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
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()

        // then
        Truth.assertThat(activity).isNotNull()
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
        Truth.assertThat(viewModel).isNotNull()
    }

    @Test
    fun `상품 클릭 시 장바구니에 담겼다는 토스트가 노출된다`() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .setup()
                .get()
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)
        val firstProduct =
            recyclerView.layoutManager
                ?.findViewByPosition(0)
                ?: error("No items found")

        // when
        firstProduct.performClick()

        // then
        val toastMessage = ShadowToast.getTextOfLatestToast()
        assertThat(toastMessage).isEqualTo("장바구니에 추가되었습니다.")
    }
}
