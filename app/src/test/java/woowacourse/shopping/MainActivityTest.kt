package woowacourse.shopping

import android.view.View
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
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
import woowacourse.shopping.ui.ProductAdapter
import woowacourse.shopping.ui.ProductViewHolder

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
    fun `툴바 타이틀이 Shopping으로 보인다`() {
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()

        val actual = activity.title
        assertThat(actual).isEqualTo("Shopping")
    }

    @Test
    fun `상품 목록 아이템 개수가 올바르게 나타난다`() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]
        val items = viewModel.products.value ?: emptyList()
        val adapter =
            ProductAdapter(
                items = items,
                onClickProduct = viewModel::addCartProduct,
            )
        val rvProducts = activity.findViewById<RecyclerView>(R.id.rv_products)
        rvProducts.adapter = adapter

        // then
        val actual = adapter.itemCount
        assertThat(actual).isEqualTo(items.size)
    }

    @Test
    fun `상품 목록에 첫 번째 상품 제목 테스트`() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]
        val adapter =
            ProductAdapter(
                items = viewModel.products.getOrAwaitValue(),
                onClickProduct = viewModel::addCartProduct,
            )
        val rvProducts = activity.findViewById<RecyclerView>(R.id.rv_products)
        rvProducts.apply {
            this.adapter = adapter
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            layout(0, 0, rvProducts.measuredWidth, rvProducts.measuredHeight)
        }

        val viewHolder = rvProducts.findViewHolderForLayoutPosition(0) as ProductViewHolder
        val binding = DataBindingUtil.getBinding<ViewDataBinding>(viewHolder.itemView)
        binding?.executePendingBindings()

        // then
        val actual = viewModel.products.value?.first()?.name
        val expected = viewHolder.itemView.findViewById<TextView>(R.id.tv_cart_product_title).text
        assertThat(actual).isEqualTo(expected)
    }

    /**
     * 이렇게 해도 되는지 모르는 테스트 코드,,,
     * */
    @Test
    fun `상품을 클릭하면 토스트 메시지가 뜬다`() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]
        val adapter =
            ProductAdapter(
                items = viewModel.products.getOrAwaitValue(),
                onClickProduct = viewModel::addCartProduct,
            )
        val rvProducts = activity.findViewById<RecyclerView>(R.id.rv_products)
        rvProducts.apply {
            this.adapter = adapter
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            layout(0, 0, rvProducts.measuredWidth, rvProducts.measuredHeight)
        }

        val viewHolder = rvProducts.findViewHolderForLayoutPosition(0) as ProductViewHolder
        val binding = DataBindingUtil.getBinding<ViewDataBinding>(viewHolder.itemView)
        binding?.executePendingBindings()

        // when
        viewHolder.itemView.performClick()

        // then
        viewModel.onProductAdded.observe(activity) {
            val toastMessage = activity.getString(R.string.cart_added)
            val shadowToast = ShadowToast.getLatestToast()

            assertThat(shadowToast).isNotNull()
            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(toastMessage)
        }
    }
}
