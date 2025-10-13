package woowacourse.shopping.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.peto.di.ViewModelFactoryInjector
import woowacourse.shopping.App
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.vm.CartViewModel

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `CartViewModel 주입 테스트`() {
        // given
        val activity =
            Robolectric.buildActivity(CartActivity::class.java)
                .create()
                .get()

        val appContainer = (activity.application as App).container
        val factory = ViewModelFactoryInjector(appContainer.dependencyContainer)

        // when
        val viewModel = ViewModelProvider(activity, factory)[CartViewModel::class.java]

        // then
        Truth.assertThat(viewModel).isNotNull()
    }
}
