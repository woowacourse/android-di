package woowacourse.shopping

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

@RunWith(RobolectricTestRunner::class)
class AppContainerTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        AppContainer.init(context)
    }

    @Test
    fun `CartRepository 의존성을 주입받을 수 있다`() {
        val cartRepository = AppContainer.get(CartRepository::class)
        assertThat(cartRepository).isNotNull
    }

    @Test
    fun `ProductRepository 의존성을 주입받을 수 있다`() {
        val productRepository = AppContainer.get(ProductRepository::class)
        assertThat(productRepository).isNotNull
    }

    @Test
    fun `CartRepository는 싱글톤으로 관리된다`() {
        val cartRepository1 = AppContainer.get(CartRepository::class)
        val cartRepository2 = AppContainer.get(CartRepository::class)
        assertThat(cartRepository1).isSameAs(cartRepository2)
    }

    @Test
    fun `ProductRepository는 싱글톤으로 관리된다`() {
        val productRepository1 = AppContainer.get(ProductRepository::class)
        val productRepository2 = AppContainer.get(ProductRepository::class)
        assertThat(productRepository1).isSameAs(productRepository2)
    }
}
