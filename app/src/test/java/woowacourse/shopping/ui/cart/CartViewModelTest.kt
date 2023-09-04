package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartViewModelTest {

    private lateinit var vm: CartViewModel
    private lateinit var cartRepository: CartRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val fakeCartProductList = mutableListOf(
        Product(
            name = "Eddie Finley",
            price = 3686,
            imageUrl = "https://www.google.com/#q=nostrum",
        ),
        Product(
            name = "Salvador Carter",
            price = 5308,
            imageUrl = "https://search.yahoo.com/search?p=euismod",
        ),
    )

    @Before
    fun setup() {
        cartRepository = mockk()
        vm = CartViewModel(cartRepository)
    }

    @Test
    fun `전체 카트 상품 목록 조회`() {
        // given
        every {
            cartRepository.getAllCartProducts()
        } answers {
            fakeCartProductList
        }

        // when
        vm.getAllCartProducts()

        // then
        assertThat(vm.cartProducts.getOrAwaitValue()).isEqualTo(fakeCartProductList)
    }

    @Test
    fun `카트 상품 목록에서 상품 삭제하면 삭제 상태가 true다`() {
        every { cartRepository.deleteCartProduct(any()) } just Runs

        vm.deleteCartProduct(0)

        assertThat(vm.onCartProductDeleted.value).isTrue
    }
}
