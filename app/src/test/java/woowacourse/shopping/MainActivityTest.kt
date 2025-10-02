package woowacourse.shopping

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
    fun `상품을 클릭하면, 토스트 메세지가 뜬다`() {
        // Given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .setup()
                .get()
        // 액티비티 객체를 직접 생성하기 때문에, 액티비티에서 할 수 있는 작업들을 해당 테스트에서 할 수 있다. espresso에 비해서 더 많은 조작이 가능
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)
        val firstProduct = recyclerView.layoutManager?.findViewByPosition(0) ?: error("No items")

        // When
        firstProduct.performClick()

        // Then
        val toastMessage = ShadowToast.getTextOfLatestToast()
        assertThat(toastMessage).isEqualTo("장바구니에 추가되었습니다.")
    }
}
