package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.ui.products.ProductsViewModel
import kotlin.reflect.full.primaryConstructor

class ConstructorReflectionTest {
    @Test
    fun `ProductsViewModel 생성자에는 의존성 파라미터가 없다`() {
        val constructor = ProductsViewModel::class.primaryConstructor!!
        assertThat(constructor.parameters).isEmpty()
    }

    @Test
    fun `인자가 없는 주 생성자를 호출해 ProductsViewModel을 생성한다`() {
        val constructor = ProductsViewModel::class.primaryConstructor!!
        val productsViewModel = constructor.call()

        assertThat(productsViewModel).isInstanceOf(ProductsViewModel::class.java)
    }
}
