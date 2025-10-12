package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.ViewModelFactory
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.fixture.FakeAppContainer
import woowacourse.shopping.fixture.PRODUCTS_FIXTURE
import woowacourse.shopping.fixture.repository.FakeCartRepository
import woowacourse.shopping.getOrAwaitValue

class CartViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CartViewModel

    @Before
    fun setup() {
        val cartRepository = FakeCartRepository(PRODUCTS_FIXTURE.toMutableList())

        val appContainer: AppContainer = FakeAppContainer(cartRepository = cartRepository)
        val viewModelFactory = ViewModelFactory(appContainer)
        viewModel = viewModelFactory.create(CartViewModel::class.java)
    }

    @Test
    fun `전체 장바구니 상품 조회 테스트`() {
        // when
        viewModel.getAllCartProducts()

        // then
        val products: List<Product> = viewModel.cartProducts.getOrAwaitValue()
        assertThat(products).hasSize(3)
        assertThat(products).isEqualTo(PRODUCTS_FIXTURE)
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
