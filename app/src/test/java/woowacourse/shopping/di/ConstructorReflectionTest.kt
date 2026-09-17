package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.products.ProductsViewModel
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ConstructorReflectionTest {
    @Test
    fun `ProductsViewModel 생성자의 파라미터 타입을 조회한다`() {
        val constructor = ProductsViewModel::class.primaryConstructor!!
        val type = constructor.parameters.map { it.type.classifier as KClass<*> }

        assertThat(type).containsExactly(ProductRepository::class, CartRepository::class)
    }

    @Test
    fun `주 생성자를 호출해 ProductsViewModel을 생성한다`() {
        val constructor = ProductsViewModel::class.primaryConstructor!!
        val productsViewModel = constructor.call(ProductRepository(), CartRepository())

        assertThat(productsViewModel).isInstanceOf(ProductsViewModel::class.java)
    }
}
