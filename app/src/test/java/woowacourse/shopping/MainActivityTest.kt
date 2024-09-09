package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
@Config(application = ShoppingApplication::class)
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
    fun injected_productRepository_is_DefaultProductRepository() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()

        // when
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        val productRepositoryField = MainViewModel::class.java.getDeclaredField("productRepository")
        productRepositoryField.isAccessible = true
        val productRepository = productRepositoryField.get(viewModel) as ProductRepository

        assertThat(productRepository).isInstanceOf(DefaultProductRepository::class.java)
    }

    @Test
    fun injected_cartRepository_is_DefaultCartRepository() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()

        // when
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        val cartRepositoryField = MainViewModel::class.java.getDeclaredField("productRepository")
        cartRepositoryField.isAccessible = true
        val cartRepository = cartRepositoryField.get(viewModel) as ProductRepository

        assertThat(cartRepository).isInstanceOf(DefaultProductRepository::class.java)
    }
}
