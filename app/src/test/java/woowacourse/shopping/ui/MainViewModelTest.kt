package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.Injector
import woowacourse.shopping.container.DiContainer
import woowacourse.shopping.createProduct
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.fake.FakeCartProductDao
import woowacourse.shopping.getProducts
import kotlin.reflect.full.createInstance

class MainViewModelTest {
    private lateinit var diContainer: DiContainer
    private lateinit var injector: Injector
    private lateinit var viewModel: MainViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        diContainer = DiContainer()
        diContainer.registerProviders {
            provider(CartProductDao::class to FakeCartProductDao::class::createInstance)
        }

        injector = Injector(diContainer)
        viewModel = injector.inject(MainViewModel::class)
    }

    @Test
    fun `카트 상품을 추가하면 카트에 상품을 담았는지 여부가 참이 된다`() {
        // given
        assertThat(viewModel.onProductAdded.value).isFalse

        // when
        val product = createProduct("글로", 1_000_000_000, "")
        viewModel.addCartProduct(product)

        // then
        assertThat(viewModel.onProductAdded.value).isTrue
    }

    @Test
    fun `모든 상품을 조회하면 모든 상품을 가져올 수 있다`() {
        // given

        // when
        viewModel.getAllProducts()

        // then
        val expected = getProducts()
        assertThat(viewModel.products.value).isEqualTo(expected)
    }
}
