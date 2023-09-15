package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
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
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartViewModelTest {

    private lateinit var vm: CartViewModel
    private lateinit var cartRepository: CartRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val fakeCartProductList = mutableListOf(
        CartProduct(
            cartProductId = 7311,
            product = Product(
                name = "Geneva Moon",
                price = 4521,
                imageUrl = "http://www.bing.com/search?q=dapibus",
            ),
            createdAt = 2124,

        ),
        CartProduct(
            cartProductId = 2254,
            product = Product(
                name = "Sonya Black",
                price = 3862,
                imageUrl = "http://www.bing.com/search?q=splendide",
            ),
            createdAt = 5752,
        ),
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        cartRepository = mockk()
        vm = CartViewModel(cartRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `전체 카트 상품 목록 조회`() {
        // given
        coEvery {
            cartRepository.getAllCartProducts()
        } coAnswers {
            fakeCartProductList
        }

        // when
        vm.getAllCartProducts()

        // then
        assertThat(vm.cartProducts.getOrAwaitValue()).isEqualTo(fakeCartProductList)
    }

    @Test
    fun `카트 상품 목록에서 상품 삭제하면 삭제 상태가 true다`() {
        coEvery { cartRepository.deleteCartProduct(any()) } just Runs

        vm.deleteCartProduct(0)

        assertThat(vm.onCartProductDeleted.value).isTrue
    }
}
