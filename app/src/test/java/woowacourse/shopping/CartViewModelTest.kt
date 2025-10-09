package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.ui.cart.CartViewModel

class CartViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `getAllCartProducts 호출 시 cartProducts LiveData가 레포지토리의 값을 반영한다`() {
        // given
        val fakeRepo =
            FakeCartRepository().apply {
                addCartProduct(ProductFixture.Snack)
                addCartProduct(ProductFixture.Juice)
            }
        val viewModel =
            CartViewModel().apply {
                cartRepository = fakeRepo
            }

        // when
        viewModel.getAllCartProducts()

        // then
        val names = viewModel.cartProducts.getOrAwaitValue().map { it.name }
        assertEquals(
            listOf(ProductFixture.Snack.name, ProductFixture.Juice.name),
            names,
        )
    }

    @Test
    fun `deleteCartProduct 호출 시 상품이 삭제되고 onCartProductDeleted가 true로 설정된다`() {
        // given
        val fakeRepo =
            FakeCartRepository().apply {
                addCartProduct(ProductFixture.Snack)
                addCartProduct(ProductFixture.Juice)
            }
        val viewModel =
            CartViewModel().apply {
                cartRepository = fakeRepo
            }

        // when
        viewModel.deleteCartProduct(1)

        // then
        assertTrue(viewModel.onCartProductDeleted.value == true)

        viewModel.getAllCartProducts()
        val names = viewModel.cartProducts.getOrAwaitValue().map { it.name }
        assertEquals(
            listOf(ProductFixture.Snack.name),
            names,
        )
    }
}
