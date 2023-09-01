package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: CartRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        cartRepository = mockk()
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun 모든_장바구니_목록을_불러오면_기존_장바구니_목록을_갱신한다() {
        // given: 불러올 장바구니 목록이 존재한다.
        val expected = listOf(
            Product("우테코 과자", 1000, "snackimage"),
            Product("우테코 쥬스", 2000, "juiceimage"),
            Product("우테코 아이스크림", 3000, "icecreamimage"),
        )
        every { cartRepository.getAllCartProducts() } returns expected

        // when: 장바구니 목록을 불러온다.
        viewModel.fetchAllCartProducts()

        // then: 기존 장바구니 목록이 갱신된다.
        val actual = viewModel.cartProducts.getOrAwaitValue()
        assert(actual == expected)
    }

    @Test
    fun 장바구니에서_상품을_제거하면_제거_상태가_참이_된다() {
        // given: 제거할 상품 ID와 상품 목록이 존재한다.
        val productIdForDeleting = 1
        every { cartRepository.deleteCartProduct(productIdForDeleting) } just Runs

        // when: 장바구니에서 상품을 제거한다.
        viewModel.deleteCartProduct(productIdForDeleting)

        // then: 제거 상태가 참이 된다.
        val actual = viewModel.onCartProductDeleted.getOrAwaitValue()
        assert(actual)
    }
}
