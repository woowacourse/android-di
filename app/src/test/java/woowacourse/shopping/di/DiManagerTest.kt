package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import com.harodi.DiManager
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class DiManagerTest {
    @Test
    fun `ViewModel을 받았을 때 해당 ViewModel이 어떤 클래스인지 알 수 있다`() {
        // given
        val diManager = DiManager()

        // when
        val productsViewModel = diManager.createInstance(ProductsViewModel::class.java)
        val cartViewModel = diManager.createInstance(CartViewModel::class.java)

        // then
        assertThat(productsViewModel).isInstanceOf(ProductsViewModel::class.java)
        assertThat(cartViewModel).isInstanceOf(CartViewModel::class.java)
    }

    @Test
    fun `ViewModel이 알맞은 파라미터 객체를 찾을 수 있다`() {
        // given
        val diManager = DiManager()

        // when
        diManager.addInstance(ProductRepository::class.java, ProductRepository())

        // then
        assertThat(diManager.hasInstance(ProductRepository::class.java)).isTrue()
        assertThat(diManager.hasInstance(CartRepository::class.java)).isFalse()
    }

    @Test
    fun `Inject 애노테이션이 붙은 필드만 주입한다`() {
        // given
        val diManager = DiManager()

        // when
        val viewModel = diManager.fieldInject(FieldInjectionTestViewModel::class.java)

        // then
        assertThat(viewModel.isInjectedRepositoryInitialized()).isTrue()
        assertThat(viewModel.isIgnoredRepositoryInitialized()).isFalse()
    }

    @Test
    fun `다른 ViewModel을 만들어도 ViewModel 객체를 생성할 수 있다`() {
        // given
        val diManager = DiManager()

        // when
        assertThat(diManager.createInstance(TestViewModel::class.java)).isInstanceOf(TestViewModel::class.java)
    }
}
