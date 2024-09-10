package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.FakeCartRepository
import woowacourse.shopping.FakeProductRepository
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.fakeProducts

class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var productsRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        productsRepository = FakeProductRepository(fakeProducts)
        cartRepository = FakeCartRepository(fakeProducts)
        mainViewModel = MainViewModel(productsRepository, cartRepository)
    }

    @Test
    fun `모든_물품들을_조회한다`() {
        mainViewModel.getAllProducts()
        val value = mainViewModel.products.getOrAwaitValue()
        assertThat(value[1].name).isEqualTo("우테코 생수")
        assertThat(value[1].price).isEqualTo(2_000)
        assertThat(value[1].imageUrl).isEqualTo("https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700")
    }

    @Test
    fun `상품을_장바구니에_추가한다`() {
        // given
        val product = Product("우테코 노트", 5_000, "http://example/image")
        val previousCartProducts = cartRepository.getAllCartProducts()

        // when
        mainViewModel.addCartProduct(product)

        // then
        val presentCartProducts = cartRepository.getAllCartProducts()
        assertThat(previousCartProducts.size).isLessThan(presentCartProducts.size)
        assertThat(presentCartProducts).contains(product)
    }
}
