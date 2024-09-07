package woowacourse.shopping.ui

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
import org.robolectric.shadows.ShadowLooper
import woowacourse.shopping.R
import woowacourse.shopping.getOrAwaitValue

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {
    private lateinit var activity: MainActivity

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

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
        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `RecyclerView 아이템 개수 테스트`() {
        // given
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // when
        viewModel.getAllProducts()
        ShadowLooper.idleMainLooper()
        val adapter = ProductAdapter(viewModel.products.getOrAwaitValue(), viewModel::addCartProduct)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)
        recyclerView.adapter = adapter

        // then
        assertThat(adapter.itemCount).isEqualTo(3)
    }
}
