package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.products.ProductsViewModel

class DIContainerTest {
    private val container = DIContainer()

    @Test
    fun `타입으로 인스턴스를 생성한다`() {
        val productRepository = container.get(ProductRepository::class)

        assertThat(productRepository).isInstanceOf(ProductRepository::class.java)
    }

    @Test
    fun `생성자에 필요한 의존성을 자동으로 주입한다`() {
        val viewModel = container.get(ProductsViewModel::class)

        assertThat(viewModel).isInstanceOf(ProductsViewModel::class.java)
    }
}
