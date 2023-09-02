package woowacourse.shopping.di

import junit.framework.TestCase.assertTrue
import org.junit.Test
import woowacourse.shopping.repository.ProductRepository

class DiContainerTest {
    @Test
    fun `DiContainer에서 상품 리포지터리 객체를 반환한다`() {
        // given & when
        val productRepository = DiContainer.get(ProductRepository::class.java)

        // then
        assertTrue(productRepository is ProductRepository)
    }

    @Test
    fun `DiContainer에서 없는 리포지터리 객체를 요청하면 예외를 발생시킨다`() {
        // given
        class MockRepository

        // when
        runCatching { DiContainer.get(MockRepository::class.java) }
            // then
            .onSuccess { throw IllegalArgumentException() }
            .onFailure { assertTrue(it is IllegalArgumentException) }
    }
}
