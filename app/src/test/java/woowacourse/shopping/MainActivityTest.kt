package woowacourse.shopping

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
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.ProductAdapter

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
    fun `Activity 실행 테스트`() {
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `모든 상품을 보여준다`() {
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)
        val adapter =
            ProductAdapter(
                items = TestFixture.products,
                onClickProduct = viewModel::addCartProduct,
            )
        recyclerView.adapter = adapter

        assertThat(adapter.itemCount).isEqualTo(3)
    }

    @Test
    fun `상품을 클릭하면 장바구니에 상품이 추가되었다는 문구를 보여준다`() {
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)
        recyclerView.performClick()

        val toastMessage = activity.getString(R.string.cart_added)
        assertThat(toastMessage).isEqualTo("장바구니에 추가되었습니다.")
    }
}
