package woowacourse.shopping

import android.content.Context
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLooper
import org.robolectric.shadows.ShadowToast
import woowacourse.bibi.di.core.ContainerBuilder
import woowacourse.bibi.di.core.Local
import woowacourse.bibi.di.core.Remote
import woowacourse.shopping.common.withOverriddenContainer
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartViewModel

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity =
            Robolectric
                .buildActivity(CartActivity::class.java)
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
                .buildActivity(CartActivity::class.java)
                .create()
                .get()
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `삭제 버튼을 누르면 토스트 메시지가 출력된다`() =
        runTest {
            // given
            val activity = launchWithFakes()
            val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_cart_products)

            ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
            val firstItem =
                recyclerView.findViewHolderForAdapterPosition(0)
                    ?: error("첫 아이템을 찾지 못했습니다.")
            ShadowToast.reset()

            val deleteButton =
                firstItem.itemView.findViewById<View>(R.id.iv_cart_product_delete)
                    ?: error("삭제 버튼을 찾지 못했습니다.")

            // when
            deleteButton.performClick()
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks()

            // then
            val toast = ShadowToast.getTextOfLatestToast()
            assertThat(toast).isEqualTo(activity.getString(R.string.cart_deleted))
        }

    private suspend fun launchWithFakes(preloadProducts: List<Product> = ProductFixture.AllProducts): CartActivity {
        val app = ApplicationProvider.getApplicationContext<Context>() as ShoppingApplication

        val fakeCartRepo =
            FakeCartRepository().apply {
                preloadProducts.forEach { addCartProduct(it) }
            }

        val testContainer =
            ContainerBuilder()
                .apply {
                    register(ProductRepository::class, Local::class) {
                        FakeProductRepository(
                            ProductFixture.AllProducts,
                        )
                    }
                    register(CartRepository::class, Local::class) { fakeCartRepo }
                }.build()

        return withOverriddenContainer(app, testContainer) {
            Robolectric
                .buildActivity(CartActivity::class.java)
                .setup()
                .get()
        }
    }
}
