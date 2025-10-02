package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

class DependencyInjectorTest {
    private val appContainer = AppContainer()
    private val dependencyInjector = DependencyInjector(appContainer)

    @Test
    fun `같은 Repository 인스턴스를 반환한다`() {
        // given
        val kClass: KClass<MainViewModel> = MainViewModel::class
        val kParameter: KParameter =
            kClass.primaryConstructor?.parameters?.firstOrNull() ?: error("")

        // when
        val firstInstance = dependencyInjector.injectInstance(kParameter)
        val secondInstance = dependencyInjector.injectInstance(kParameter)

        // then
        assertThat(firstInstance).isSameInstanceAs(secondInstance)
    }

    @Test
    fun `MainViewModel에 필요한 인자를 자동 주입해 인스턴스를 생성한다`() {
        // given & when
        val mainViewModel = dependencyInjector.inject<MainViewModel>()

        // then
        assertThat(mainViewModel).isNotNull()
    }

    @Test
    fun `CartViewModel에 필요한 인자를 자동 주입해 인스턴스를 생성한다`() {
        // given & when
        val cartViewModel = dependencyInjector.inject<CartViewModel>()

        // then
        assertThat(cartViewModel).isNotNull()
    }
}
