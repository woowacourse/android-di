package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.ProductRepository

class DIContainerTest {
    private val container = DIContainer()

    @Test
    fun `타입으로 인스턴스를 생성한다`() {
        val productRepository = container.get(ProductRepository::class)

        assertThat(productRepository).isInstanceOf(ProductRepository::class.java)
    }
}
