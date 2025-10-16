package woowacourse.shopping.ui.cart

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import woowacourse.shopping.Container
import woowacourse.shopping.DEFAULT_CART_PRODUCT
import woowacourse.shopping.annotation.Room
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.ui.FakeCartRepository
import woowacourse.shopping.ui.InstantTaskExecutorExtension
import woowacourse.shopping.ui.getOrAwaitValue

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class CartViewModelTest {
    private lateinit var container: Container
    private lateinit var viewModel: CartViewModel
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        container =
            Container().apply {
                bind(
                    type = CartRepository::class,
                    provider = { FakeCartRepository() },
                    qualifier = Room::class,
                )
            }
        viewModel = container.get()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @DisplayName("장바구니에 담긴 상품 목록을 가져온다")
    @Test
    fun getAllCartProductsTest() =
        runTest(testDispatcher) {
            // when
            viewModel.getAllCartProducts()
            advanceUntilIdle()
            val products = viewModel.cartProducts.getOrAwaitValue()

            // then
            products shouldContain DEFAULT_CART_PRODUCT
        }

    @DisplayName("장바구니에 담긴 물건을 삭제하면 상태가 변경된다")
    @Test
    fun deleteCartProductTest() =
        runTest(testDispatcher) {
            // given
            val id = 1L

            // when
            viewModel.deleteCartProduct(id)
            advanceUntilIdle()
            val deleted = viewModel.onCartProductDeleted.getOrAwaitValue()

            // then
            deleted.shouldBeTrue()
        }
}
