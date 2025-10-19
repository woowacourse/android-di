package woowacourse.shopping.di

import androidx.test.core.app.ApplicationProvider
import com.example.di.di.InjectorViewModelFactory
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

@RunWith(RobolectricTestRunner::class)
@Config(application = ShoppingApplication::class)
class InjectorViewModelFactoryTest {
    private val application = ApplicationProvider.getApplicationContext<ShoppingApplication>()

    @Test
    fun `MainViewModel이 자동 의존성 주입되어 생성된다`() {
        // given & when
        val viewModel: MainViewModel =
            InjectorViewModelFactory(
                dependencyInjector = (application as ShoppingApplication).dependencyInjector,
                scopeHolder = this,
            ).create(MainViewModel::class.java)

        // then
        assertThat(viewModel).isInstanceOf(MainViewModel::class.java)
    }

    @Test
    fun `CartViewModel이 자동 의존성 주입되어 생성된다`() {
        // given & when
        val viewModel: CartViewModel =
            InjectorViewModelFactory(
                dependencyInjector = (application as ShoppingApplication).dependencyInjector,
                scopeHolder = this,
            ).create(CartViewModel::class.java)

        // then
        assertThat(viewModel).isInstanceOf(CartViewModel::class.java)
    }
}
