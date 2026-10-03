package woowacourse.shopping

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.model.Product

class InMemoryCartRepositoryTest {
    @Test
    fun `InMemory 구현도 실제 상품 식별자로 삭제한다`() =
        runTest {
            val repository = InMemoryCartRepository()
            repository.addCartProduct(Product("첫 상품", 1_000, "first"))
            repository.addCartProduct(Product("둘째 상품", 2_000, "second"))
            val products = repository.getAllCartProducts()

            repository.deleteCartProduct(products.last().id)

            assertThat(products.map { it.id }).containsExactly(1L, 2L)
            assertThat(repository.getAllCartProducts().map { it.name }).containsExactly("첫 상품")
        }
}
