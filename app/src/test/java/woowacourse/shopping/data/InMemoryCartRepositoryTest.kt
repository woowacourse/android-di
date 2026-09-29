package woowacourse.shopping.data

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.model.Product

class InMemoryCartRepositoryTest {
    @Test
    fun `삽입 순서대로 조회하고 삭제한 ID를 재사용하지 않는다`() =
        runTest {
            val repository = InMemoryCartRepository()
            val before = System.currentTimeMillis()
            repository.addCartProduct(Product("첫째", 100, ""))
            repository.addCartProduct(Product("둘째", 200, ""))
            repository.addCartProduct(Product("셋째", 300, ""))
            val after = System.currentTimeMillis()

            val inserted = repository.getAllCartProducts()
            assertThat(inserted.map { it.id }).containsExactly(1L, 2L, 3L)
            assertThat(inserted.map { it.name }).containsExactly("첫째", "둘째", "셋째")
            assertThat(inserted.map { it.createdAt }).allMatch { it in before..after }

            repository.deleteCartProduct(2L)
            repository.addCartProduct(Product("넷째", 400, ""))

            assertThat(repository.getAllCartProducts().map { it.id }).containsExactly(1L, 3L, 4L)
            assertThat(repository.getAllCartProducts().map { it.name }).containsExactly("첫째", "셋째", "넷째")
        }

    @Test
    fun `없는 ID를 삭제해도 목록은 변하지 않는다`() =
        runTest {
            val repository = InMemoryCartRepository()
            repository.addCartProduct(Product("상품", 100, ""))

            repository.deleteCartProduct(999L)

            assertThat(repository.getAllCartProducts()).hasSize(1)
        }
}
