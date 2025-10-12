package woowacourse.shopping.di

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.di.auto.AutoInjectingViewModelFactory
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.main.MainActivity
import woowacourse.shopping.ui.main.MainViewModel
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

@RunWith(RobolectricTestRunner::class)
class AutoInjectingViewModelFactoryTest {
    @get:Rule
    val instant = InstantTaskExecutorRule()

    @Test
    fun `메인_액티비티에서_ViewModel이_정상적으로_주입된다`() {
        // given
        val activity = Robolectric.buildActivity(MainActivity::class.java).setup().get()

        // when
        val mainViewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(mainViewModel).isNotNull()
    }

    @Test
    fun `메인_액티비티에서_두_ViewModel이_같은_Repository를_주입받는다`() {
        // given
        val activity = Robolectric.buildActivity(MainActivity::class.java).setup().get()
        val factory = AutoInjectingViewModelFactory(AppContainer)

        // when
        val mainViewModel = ViewModelProvider(activity, factory)[MainViewModel::class.java]
        val cartViewModel = ViewModelProvider(activity, factory)[CartViewModel::class.java]

        // then
        val mainCartRepository = extractPrivateRepository(mainViewModel, "cartRepository")
        val cartCartRepository = extractPrivateRepository(cartViewModel, "cartRepository")
        assertThat(mainCartRepository === cartCartRepository).isTrue()
    }

    private fun extractPrivateRepository(
        viewModel: ViewModel,
        fieldName: String,
    ): Any? {
        val kClass = viewModel::class
        val property = kClass.declaredMemberProperties.first { it.name == fieldName }
        property.isAccessible = true
        return property.getter.call(viewModel)
    }
}
