package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.ViewModelFactory
import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.fixture.FakeAppContainer
import woowacourse.shopping.fixture.model.CART_PRODUCTS_FIXTURE
import woowacourse.shopping.fixture.repository.FakeCartRepository
import woowacourse.shopping.getOrAwaitValue

@ExperimentalCoroutinesApi
class CartViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: CartViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val cartRepository = FakeCartRepository(CART_PRODUCTS_FIXTURE.toMutableList())
        val container =
            FakeAppContainer().apply {
                register(CartRepository::class, cartRepository)
            }
        val dependencyInjector = DependencyInjector(container)
        val viewModelFactory = ViewModelFactory(dependencyInjector)
        viewModel = viewModelFactory.create(CartViewModel::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `전체 장바구니 상품 조회 테스트`() {
        // when
        viewModel.getAllCartProducts()

        // then
        val products: List<CartProduct> = viewModel.cartProducts.getOrAwaitValue()
        assertThat(products).hasSize(3)
        assertThat(products).isEqualTo(CART_PRODUCTS_FIXTURE)
    }

    @Test
    fun `장바구니 상품 삭제 테스트`() {
        // when
        viewModel.deleteCartProduct(0)

        // then
        val onProductAdded: Boolean = viewModel.onCartProductDeleted.getOrAwaitValue()
        assertThat(onProductAdded).isTrue
    }
}
