package woowacourse.shopping.di

import junit.framework.TestCase.assertTrue
import org.junit.Test
import woowacourse.shopping.data.ProductSampleRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class DiContainerTest {
    private class FameProductRepository : ProductRepository {
        override fun getAllProducts(): List<Product> = emptyList()
    }

    private class FakeCartRepository : CartRepository {
        override fun addCartProduct(product: Product) {}

        override fun getAllCartProducts(): List<Product> = emptyList()

        override fun deleteCartProduct(id: Int) {}
    }

    private class FakeDiContainer : DiContainer() {
        private val productRepository: ProductRepository = ProductSampleRepository()
        private val cartRepository: CartRepository = FakeCartRepository()
    }

    private val fakeDiContainer = FakeDiContainer()

    @Test
    fun `DiContainer에서 상품 리포지터리 객체를 반환한다`() {
        // given & when
        val productRepository = fakeDiContainer.get(ProductRepository::class.java)

        // then
        assertTrue(productRepository is ProductRepository)
    }

    @Test
    fun `DiContainer에서 장바구니 리포지터리 객체를 반환한다`() {
        // given & when
        val cartRepository = fakeDiContainer.get(CartRepository::class.java)

        // then
        assertTrue(cartRepository is CartRepository)
    }

    @Test
    fun `DiContainer에서 없는 리포지터리 객체를 요청하면 예외를 발생시킨다`() {
        // given
        class MockRepository

        // when
        runCatching { fakeDiContainer.get(MockRepository::class.java) }
            // then
            .onSuccess { throw IllegalArgumentException() }
            .onFailure { assertTrue(it is IllegalArgumentException) }
    }
}
