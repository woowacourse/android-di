package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.di.component.DiSingletonComponent
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ViewModelInjectionTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockCartRepository: CartRepository

    @Mock
    private lateinit var mockProductRepository: ProductRepository

    @Mock
    private lateinit var mockCartProductDao: CartProductDao

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        DiSingletonComponent.provide(CartRepository::class, mockCartRepository)
        DiSingletonComponent.provide(ProductRepository::class, mockProductRepository)
        DiSingletonComponent.provide(CartProductDao::class, mockCartProductDao)
    }

    @Test
    fun testCartViewModelInjection() {
        val factory = inject()
        val viewModel = factory.create(CartViewModel::class.java)

        assertNotNull(viewModel)
    }

    @Test
    fun testMainViewModelInjection() {
        val factory = inject()
        val viewModel = factory.create(MainViewModel::class.java)

        assertNotNull(viewModel)
    }
}
