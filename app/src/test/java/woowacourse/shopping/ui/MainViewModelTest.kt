package woowacourse.shopping.ui

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product
import woowacourse.shopping.provider.DefaultViewModelTest
import woowacourse.shopping.provider.Dummy
import woowacourse.shopping.provider.Fake
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

internal class MainViewModelTest : DefaultViewModelTest() {

    private lateinit var sut: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        productRepository = Fake.ProductRepository()
        cartRepository = Fake.CartRepository()
        sut = MainViewModel(productRepository, cartRepository)
    }

    @Test
    fun `전체 상품을 조회한다`() {
        // given
        val products = productRepository.getAllProducts()

        // when
        sut.fetchAllProducts()

        // then
        assertEquals(products, sut.products.getOrAwaitValue())
    }

    @Test
    fun `장바구니에 제품을 추가하면 제품 추가 상태로 변경한다`() {
        // given
        val addProduct = Dummy.Product()

        // when
        sut.addCartProduct(addProduct)

        // then
        assertTrue(sut.onProductAdded.getOrAwaitValue())
    }
}
