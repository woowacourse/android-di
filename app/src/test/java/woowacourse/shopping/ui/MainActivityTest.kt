package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowActivity
import woowacourse.shopping.R
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.ui.cart.CartActivity

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `장바구니 버튼을 클릭하면 장바구니 화면으로 이동한다`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()

        val menu = activity.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).menu
        activity.onCreateOptionsMenu(menu)

        val cartItem = menu.findItem(R.id.cart).actionView!!

        // when
        cartItem.performClick()

        val shadowActivity: ShadowActivity = Shadows.shadowOf(activity)
        val startedIntent = shadowActivity.nextStartedActivity

        // then
        assertEquals(startedIntent.component?.className, CartActivity::class.java.name)
    }

    @Test
    fun `리사이클러뷰 아이템 개수는 DefaultProductRepository의 아이템 수와 같다`() {
        // given
        val activityController = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .start()

        val activity = activityController.get()
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)

        // when
        val productRepository = DefaultProductRepository()
        val actual = productRepository.getAllProducts().size

        // then
        assertEquals(recyclerView.adapter?.itemCount, actual)
    }

    @Test
    fun `리사이클러뷰 아이템을 클릭하면 onProductAdded가 true로 변경된다`() {
        // given
        val activityController = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .start()
            .visible()

        val activity = activityController.get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)

        // when
        recyclerView.findViewHolderForAdapterPosition(0)?.itemView?.performClick()

        // then
        assertTrue(viewModel.onProductAdded.value!!)
    }
}
