package woowacourse.shopping.ui

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.MainDispatcherRule
import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class ShoppingViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `상품 저장이 완료된 뒤 추가 이벤트를 보낸다`() =
        runTest {
            val repository = FakeCartRepository()
            val viewModel = ProductsViewModel().apply { cartRepository = repository }
            val savedAtEvent = mutableListOf<List<CartProduct>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.onProductAdded.collect { savedAtEvent.add(repository.products.toList()) }
            }

            viewModel.addCartProduct(Product(name = "과자", price = 10_000, imageUrl = ""))
            assertThat(repository.products).isEmpty()
            advanceUntilIdle()

            assertThat(repository.products.map { it.name }).containsExactly("과자")
            assertThat(savedAtEvent).containsExactly(repository.products.toList())
        }

    @Test
    fun `조회가 완료된 뒤 장바구니 상태를 갱신한다`() =
        runTest {
            val products = listOf(cartProduct(10L), cartProduct(30L))
            val viewModel =
                CartViewModel().apply {
                    cartRepository = FakeCartRepository(products)
                }

            viewModel.getAllCartProducts()
            assertThat(viewModel.uiState.value.cartProducts).isEmpty()
            advanceUntilIdle()

            assertThat(viewModel.uiState.value.cartProducts).containsExactlyElementsOf(products)
        }

    @Test
    fun `중간 상품의 ID로 삭제하고 목록을 갱신한 뒤 삭제 이벤트를 보낸다`() =
        runTest {
            val products = listOf(cartProduct(10L), cartProduct(30L), cartProduct(70L))
            val repository = FakeCartRepository(products)
            val viewModel = CartViewModel().apply { cartRepository = repository }
            val statesAtEvent = mutableListOf<List<CartProduct>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.onCartProductDeleted.collect {
                    statesAtEvent.add(viewModel.uiState.value.cartProducts)
                }
            }
            viewModel.getAllCartProducts()
            advanceUntilIdle()

            viewModel.deleteCartProduct(30L)
            assertThat(viewModel.uiState.value.cartProducts).containsExactlyElementsOf(products)
            advanceUntilIdle()

            val expected = listOf(products[0], products[2])
            assertThat(repository.products).containsExactlyElementsOf(expected)
            assertThat(viewModel.uiState.value.cartProducts).containsExactlyElementsOf(expected)
            assertThat(statesAtEvent).containsExactly(expected)
        }

    private fun cartProduct(id: Long): CartProduct =
        CartProduct(
            id = id,
            name = "과자",
            price = 10_000,
            imageUrl = "",
            createdAt = 1_700_000_000_000L,
        )
}
