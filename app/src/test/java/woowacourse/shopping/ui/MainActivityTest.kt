package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.common.truth.Truth
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
    private lateinit var activity: MainActivity

    @Before
    fun setup() {
        activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .setup()
            .get()
    }

    @Test
    fun `Activity 실행 테스트`() {
        // then
        Truth.assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        Truth.assertThat(viewModel).isNotNull()
    }

    @Test
    fun `상품 클릭시 상품 추가 토스트바가 출력 된다`() {
        //given
        val firstItem =
            activity.findViewById<RecyclerView>(R.id.rv_products)
                .layoutManager?.findViewByPosition(
                    0
                )
        //when
        firstItem?.performClick()

        // then: 토스트 메시지 검증
        val latestText = ShadowToast.getTextOfLatestToast()

        Truth.assertThat(latestText).isEqualTo("장바구니에 추가되었습니다.")
    }
}