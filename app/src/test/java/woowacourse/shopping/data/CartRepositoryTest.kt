package woowacourse.shopping.data

import androidx.room.Room
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.model.Product

@RunWith(RobolectricTestRunner::class)
class CartRepositoryTest {
    @Test
    fun `Room에 저장한 상품을 식별자로 삭제한다`() {
        runBlocking {
            val database =
                Room
                    .inMemoryDatabaseBuilder(
                        RuntimeEnvironment.getApplication(),
                        ShoppingDatabase::class.java,
                    ).build()
            try {
                val repository = DefaultCartRepository(database.cartProductDao())
                repository.addCartProduct(Product("첫 번째", 1000, "first"))
                repository.addCartProduct(Product("두 번째", 2000, "second"))
                repository.addCartProduct(Product("세 번째", 3000, "third"))

                val saved = repository.getAllCartProducts()
                assertThat(saved.map { it.name }).containsExactly("첫 번째", "두 번째", "세 번째")
                assertThat(saved.map { it.id }.distinct()).hasSize(3)
                assertThat(saved.all { it.createdAt > 0L }).isTrue()

                repository.deleteCartProduct(saved.first { it.name == "두 번째" }.id)

                assertThat(repository.getAllCartProducts().map { it.name })
                    .containsExactly("첫 번째", "세 번째")
            } finally {
                database.close()
            }
        }
    }
}
