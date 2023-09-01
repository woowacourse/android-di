package woowacourse.shopping.ui.main

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
import woowacourse.shopping.repository.ProductRepository

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        productRepository = mockk()
        cartRepository = mockk()

        viewModel = MainViewModel(
            productRepository = productRepository,
            cartRepository = cartRepository,
        )
    }

    @Test
    fun 장바구니_목록에_제품을_추가하면_상품_추가_상태가_참이_된다() {
        // given: 장바구니에 추가할 상품이 존재한다.
        val product = mockk<Product>()
        every { cartRepository.addCartProduct(product) } just Runs

        // when: 장바구니에 상품을 추가한다.
        viewModel.addCartProduct(product)

        // then: 상품 추가 상태가 참으로 변경된다.
        val actual = viewModel.onProductAdded.getOrAwaitValue()
        val expected = true
        assert(actual == expected)
    }

    @Test
    fun 상품_목록을_모두_불러왔을_때_기존_상품_목록을_갱신한다() {
        // given: 상품 레퍼지토리에 상품 목록이 존재한다.
        val expected = listOf(
            Product("우테코 과자", 1000, "snackimage"),
            Product("우테코 쥬스", 2000, "juiceimage"),
            Product("우테코 아이스크림", 3000, "icecreamimage"),
        )
        every { productRepository.getAllProducts() } returns expected

        // when: 상품 목록을 모두 불러온다.
        viewModel.fetchAllProducts()

        // then: 상품 목록이 갱신된다.
        val actual = viewModel.products.getOrAwaitValue()
        assert(actual == expected)
    }
}
