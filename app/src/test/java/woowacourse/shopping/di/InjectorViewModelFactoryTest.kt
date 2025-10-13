package woowacourse.shopping.di

import androidx.test.core.app.ApplicationProvider
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
    fun `MainViewModel의 @Inject 필드가 올바르게 주입된다`() {
        // given
        val clazz = MainViewModel::class.java

        // when
        val viewModel: MainViewModel = application.injectorViewModelFactory.create(clazz)

        // then
        assertThat(viewModel.cartRepository).isNotNull()
        assertThat(viewModel.productRepository).isNotNull()
    }

    @Test
    fun `CartViewModel의 @Inject 필드가 올바르게 주입된다`() {
        // given
        val clazz = CartViewModel::class.java

        // when
        val viewModel: CartViewModel = application.injectorViewModelFactory.create(clazz)

        // then
        assertThat(viewModel.cartRepository).isNotNull()
    }
}