package woowacourse.shopping.data

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import woowacourse.shopping.model.Product

class InMemoryCartRepositoryTest {
    @Test
    fun `상품을 추가하고 조회한 뒤 삭제한다`() =
        runTest {
            val repository = InMemoryCartRepository()
            val product = Product(name = "우테코 과자", price = 1_000, imageUrl = "image")

            repository.addCartProduct(product)

            val savedProduct = repository.getAllCartProducts().single()
            assertThat(savedProduct.id).isEqualTo(1L)
            assertThat(savedProduct.name).isEqualTo(product.name)
            assertThat(savedProduct.price).isEqualTo(product.price)
            assertThat(savedProduct.imageUrl).isEqualTo(product.imageUrl)
            assertThat(savedProduct.createdAt).isGreaterThan(0L)

            repository.deleteCartProduct(savedProduct.id)

            assertThat(repository.getAllCartProducts()).isEmpty()
        }
}
