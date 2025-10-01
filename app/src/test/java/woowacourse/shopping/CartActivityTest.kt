package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.di.ViewModelFactoryInjector
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

        val factory = ViewModelFactoryInjector.create(CartViewModel::class)

        // when
        val viewModel = ViewModelProvider(activity, factory)[CartViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }
}
