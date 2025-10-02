package woowacourse.shopping.cart

import android.widget.ImageView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowToast
import woowacourse.shopping.R
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartViewModel

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric.buildActivity(CartActivity::class.java).create().get()
        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric.buildActivity(CartActivity::class.java).create().get()
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `장바구니 아이템의 취소 버튼을 누르면 토스트 메시지가 나온다`() {
        // given
        val expected = "품목이 삭제되었습니다."

        val mainActivity = Robolectric.buildActivity(MainActivity::class.java).setup().get()
        val mainRecyclerView = mainActivity.findViewById<RecyclerView>(R.id.rv_products)
        mainRecyclerView.layoutManager?.findViewByPosition(0)?.performClick()

        val cartActivity = Robolectric.buildActivity(CartActivity::class.java).setup().get()
        val cartRecyclerView = cartActivity.findViewById<RecyclerView>(R.id.rv_cart_products)
        val item = cartRecyclerView.layoutManager?.findViewByPosition(0)

        // when
        item?.findViewById<ImageView>(R.id.iv_cart_product_delete)?.performClick()

        // then
        val actual = ShadowToast.getTextOfLatestToast()
        assertThat(actual).isEqualTo(expected)
    }
}
