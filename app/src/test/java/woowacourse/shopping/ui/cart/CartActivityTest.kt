package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.common.truth.Truth.assertThat
import com.kmlibs.supplin.Injector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLooper
import woowacourse.shopping.R
import woowacourse.shopping.getOrAwaitValue

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {
    private lateinit var activity: CartActivity

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        activity =
            Robolectric
                .buildActivity(CartActivity::class.java)
                .create()
                .get()
    }

    @After
    fun tearDown() {
        Injector.setModules {
            removeAllModules()
        }
    }

    @Test
    fun `Activity 실행 테스트`() {
        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `RecyclerView 아이템 개수 테스트`() {
        // given
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // when
        viewModel.getAllCartProducts()
        ShadowLooper.idleMainLooper()
        val adapter =
            CartProductAdapter(
                items = viewModel.cartProducts.getOrAwaitValue(),
                onClickDelete = viewModel::deleteCartProduct,
                dateFormatter = DateFormatter(activity),
            )
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_cart_products)
        recyclerView.adapter = adapter

        // then
        assertThat(adapter.itemCount).isEqualTo(0)
    }
}
