package woowacourse.shopping.ui

import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.R
import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartProductAdapter
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.main.MainActivity

@RunWith(RobolectricTestRunner::class)
class RobolectricStudy {

    @Test
    fun `visible() 을 사용하면 리사이클러뷰 아이템을 클릭할 수 있다`() {
        // given
        val activityController = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .start()
            .visible() // UI가 화면에 표시되었다는 것을 가정할 수 있도록 해줌

        val activity = activityController.get()
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)

        // when
        val isClicked = recyclerView.findViewHolderForAdapterPosition(0)?.itemView?.performClick()!!

        // then
        assertTrue(isClicked)
    }

    @Test
    fun `visible() 을 사용하지 않으면 뷰홀더를 가져오지 못해 null이다`() {
        // given
        val activityController = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .start()

        val activity = activityController.get()
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)

        // visible() 이 없는 경우 뷰홀더 자체가 화면에 표시되지 않기 때문에 아이템 뷰를 찾아올 수 없음, 즉 null이 된다
        // when
        val itemView = recyclerView.findViewHolderForAdapterPosition(0)?.itemView

        // then
        assertNull(itemView)
    }

    @Test
    fun `리사이클러뷰를 수동으로 그려주면 클릭할 수 있다`() {
        // given
        val activityController = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .start()

        val activity = activityController.get()

        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)

        // Robolectric 테스트 환경에서 리사이클러뷰가 화면에 표시되고 클릭 이벤트를 발생시킬 수 있는 상태로 만듦
        recyclerView.measure(0, 0)
        recyclerView.layout(0, 0, 100, 10000)

        // when
        val isClicked = recyclerView.findViewHolderForAdapterPosition(0)?.itemView?.performClick()!!

        // then
        assertTrue(isClicked)
    }

    @Test
    fun `뷰홀더가 보이지 않으면 null이 반환된다`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .start()
            .get()

        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)

        // when
        val itemView = recyclerView.findViewHolderForAdapterPosition(0)?.itemView

        // then
        assertNull(itemView)
    }

    @Test
    fun `장바구니에 아이템을 추가한 어댑터로 교체하면 아이템이 추가되어 그려지고 X버튼을 가져올 수 있다`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .start()
            .visible()
            .get()

        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        val products = listOf(Product("과자", 1000, ""))
        val newAdapter = CartProductAdapter(
            items = products.map { it.toCartProduct(0) },
            dateFormatter = DateFormatter(activity),
            onClickDelete = viewModel::deleteCartProduct,
        )
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_cart_products)
        recyclerView.adapter = newAdapter

        Robolectric.flushForegroundThreadScheduler() // UI가 변경되기 까지 기다린다

        // when
        val deleteImageView = recyclerView.findViewHolderForAdapterPosition(0)?.itemView
            ?.findViewById<ImageView>(R.id.iv_cart_product_delete)

        // then
        assertNotNull(deleteImageView)
    }
}
