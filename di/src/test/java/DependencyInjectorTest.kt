import com.google.common.truth.Truth.assertThat
import com.woowacourse.di.DependencyInjector
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class DependencyInjectorTest {
    private val cartProductDao = mockk<FakeCartProductDao>()

    @Before
    fun setUp() {
        DependencyInjector.initialize(cartProductDao)
    }

    @Test
    fun `ProductRepository 주입 테스트`() {
        val productRepository = DependencyInjector.findInstance(FakeProductDefaultRepository::class)
        assertThat(productRepository).isNotNull()
    }

    @Test
    fun `CartRepository 주입 테스트`() {
        val cartRepository = DependencyInjector.findInstance(FakeCartDefaultRepository::class)
        assertThat(cartRepository).isNotNull()
    }

    @Test
    fun `MainViewModel 주입 테스트`() {
        val mainViewModel = DependencyInjector.findInstance(MainViewModel::class)
        assertThat(mainViewModel).isNotNull()
    }

    @Test
    fun `CartViewModel 주입 테스트`() {
        val cartViewModel = DependencyInjector.findInstance(CartViewModel::class)
        assertThat(cartViewModel).isNotNull()
    }
}
