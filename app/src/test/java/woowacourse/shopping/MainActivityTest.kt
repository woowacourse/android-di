package woowacourse.shopping

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowToast
import woowacourse.shopping.di.ContainerBuilder
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel

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
    fun `상품을 클릭하면 토스트 메세지가 출력된다`() {
        // given
        val activity = launchMainWithFakes()
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)
        val firstProduct =
            recyclerView.layoutManager
                ?.findViewByPosition(0)
                ?: error("아이템을 찾지 못했습니다")

        // when
        firstProduct.performClick()

        // then
        val toastMessage = ShadowToast.getTextOfLatestToast()
        assertThat(toastMessage).isEqualTo("장바구니에 추가되었습니다.")
    }

    private fun launchMainWithFakes(): MainActivity {
        val testContainer =
            ContainerBuilder()
                .apply {
                    register(CartRepository::class) { FakeCartRepository() }
                    register(ProductRepository::class) { FakeProductRepository(ProductFixture.AllProducts) }
                }.build()

        val app = ApplicationProvider.getApplicationContext<Context>() as ShoppingApplication
        app.overrideContainerForTest(testContainer)

        return Robolectric
            .buildActivity(MainActivity::class.java)
            .setup()
            .get()
    }
}
