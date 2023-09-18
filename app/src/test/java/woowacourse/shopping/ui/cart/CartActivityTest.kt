package woowacourse.shopping.ui.cart

import android.widget.ImageView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.R
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.main.MainActivity
import woowacourse.shopping.ui.main.MainViewModel
import kotlin.reflect.full.functions

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `장바구니에 상품을 담지 않은 경우 상품의 개수는 0이다`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .start()
            .get()

        // when
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        assertEquals(viewModel.cartProducts.value?.size, 0)
    }

    @Test
    fun `장바구니 아이템이 1개가 있을 때 X버튼을 클릭하면 삭제되어 장바구니 아이템의 개수는 0개가 된다`() {
        // given
        val mainActivity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .start()
            .visible()
            .get()

        val cartActivity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .start()
            .visible()
            .get()

        val product = Product("과자", 1000, "")

        val mainViewModel = ViewModelProvider(mainActivity)[MainViewModel::class.java]
        val cartViewModel = ViewModelProvider(cartActivity)[CartViewModel::class.java]

        // 장바구니에 아이템 추가
        val mainViewModelFunctions = mainViewModel::class.functions
        val addFunction = mainViewModelFunctions.find {
            it.name == "addCartProduct"
        }
        addFunction?.call(mainViewModel, product)
        cartViewModel.getAllCartProducts()

        Robolectric.flushForegroundThreadScheduler()

        // X 버튼 클릭
        val recyclerView = cartActivity.findViewById<RecyclerView>(R.id.rv_cart_products)
        val deleteImageView = recyclerView.findViewHolderForAdapterPosition(0)?.itemView
            ?.findViewById<ImageView>(R.id.iv_cart_product_delete)

        deleteImageView?.performClick()
        cartViewModel.getAllCartProducts()

        // then
        assertEquals(cartViewModel.cartProducts.value?.size, 0)
    }
}
