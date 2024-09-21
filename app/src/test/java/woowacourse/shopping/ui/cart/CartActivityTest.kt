package woowacourse.shopping.ui.cart

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
import woowacourse.shopping.R

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var activity: CartActivity

    @Before
    fun setUp() {
        activity =
            Robolectric
                .buildActivity(CartActivity::class.java)
                .create()
                .get()
    }

    @Test
    fun `Activity 실행 테스트`() {
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `장바구니에 담긴 상품이 2개라면 2개의 상품을 보여준다`() {
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        val adapter =
            CartProductAdapter(
                cartHandler = viewModel,
                dateFormatter = DateFormatter(activity),
            )
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_cart_products)
        recyclerView.adapter = adapter

        assertThat(adapter.itemCount).isEqualTo(2)
    }
}
