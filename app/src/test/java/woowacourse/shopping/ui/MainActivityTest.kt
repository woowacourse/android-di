package woowacourse.shopping.ui

import androidx.appcompat.widget.Toolbar
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
    private lateinit var activity: MainActivity

    @Before
    fun setUp() {
        activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()
    }

    @Test
    fun `Activity_실행_테스트`() {
        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel_주입_테스트`() {
        // given
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `툴바가_설정되어_있는지_확인`() {
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        assertThat(toolbar).isNotNull()
        assertThat(activity.supportActionBar).isNotNull()
    }

    @Test
    fun `상품_목록의_상품을_클릭하면_토스트_메시지가_표시된다`() {
        // when
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)
        val firstProduct = recyclerView.findViewHolderForAdapterPosition(0)?.itemView ?: return

        firstProduct.performClick()

        // then
        val latestToastText = ShadowToast.getTextOfLatestToast()
        assertThat(latestToastText).isEqualTo("장바구니에 추가되었습니다.")
    }
}
