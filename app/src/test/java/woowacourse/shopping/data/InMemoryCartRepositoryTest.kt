package woowacourse.shopping.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.model.Product

class InMemoryCartRepositoryTest {
    private val repository = InMemoryCartRepository()
    private val product = Product(name = "과자", price = 10_000, imageUrl = "snack.jpg")

    @Test
    fun `상품 정보와 담은 시각을 식별자와 함께 저장한다`() =
        runTest {
            val beforeInsert = System.currentTimeMillis()

            repository.addCartProduct(product)
            val saved = repository.getAllCartProducts().single()

            assertThat(saved.id).isPositive()
            assertThat(saved.name).isEqualTo(product.name)
            assertThat(saved.price).isEqualTo(product.price)
            assertThat(saved.imageUrl).isEqualTo(product.imageUrl)
            assertThat(saved.createdAt).isBetween(beforeInsert, System.currentTimeMillis())
        }

    @Test
    fun `중복 상품도 ID로 삭제하고 삭제한 식별자를 재사용하지 않는다`() =
        runTest {
            repeat(3) { repository.addCartProduct(product) }
            val saved = repository.getAllCartProducts()

            repository.deleteCartProduct(saved[1].id)
            assertThat(repository.getAllCartProducts()).containsExactly(saved[0], saved[2])
            repository.deleteCartProduct(saved[2].id)
            repository.addCartProduct(product)

            val remaining = repository.getAllCartProducts()
            assertThat(remaining).hasSize(2)
            assertThat(remaining.first()).isEqualTo(saved.first())
            assertThat(remaining.last().id).isGreaterThan(saved.last().id)
        }

    @Test
    fun `조회 결과는 이후 저장소 변경의 영향을 받지 않는다`() =
        runTest {
            repository.addCartProduct(product)
            val snapshot = repository.getAllCartProducts()

            repository.addCartProduct(product)
            repository.deleteCartProduct(snapshot.single().id)

            assertThat(snapshot).hasSize(1)
            assertThat(repository.getAllCartProducts().single().id).isNotEqualTo(snapshot.single().id)
        }

    @Test
    fun `없는 ID를 삭제해도 저장한 상품은 유지된다`() =
        runTest {
            repository.addCartProduct(product)
            val saved = repository.getAllCartProducts()

            repository.deleteCartProduct(Long.MAX_VALUE)

            assertThat(repository.getAllCartProducts()).containsExactlyElementsOf(saved)
        }

    @Test
    fun `여러 코루틴이 동시에 저장해도 식별자가 중복되거나 상품이 사라지지 않는다`() =
        runTest {
            coroutineScope {
                repeat(100) {
                    launch(Dispatchers.Default) { repository.addCartProduct(product) }
                }
            }

            val saved = repository.getAllCartProducts()
            assertThat(saved).hasSize(100)
            assertThat(saved.map { it.id }).doesNotHaveDuplicates()
        }

    @Test
    fun `새 메모리 저장소에는 이전 인스턴스의 상품이 남지 않는다`() =
        runTest {
            repository.addCartProduct(product)

            assertThat(InMemoryCartRepository().getAllCartProducts()).isEmpty()
        }
}
