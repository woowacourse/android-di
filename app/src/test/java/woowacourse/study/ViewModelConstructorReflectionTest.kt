package woowacourse.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ViewModelConstructorReflectionTest {
    @Test
    fun `ProductsViewModel 생성자의 파라미터 타입을 확인한다`() {
        // given
        val constructor = ProductsViewModel::class.primaryConstructor!!

        val parameterTypes =
            constructor.parameters.map { parameter ->
                parameter.type.classifier as KClass<*>
            }

        assertThat(parameterTypes).containsExactly(
            ProductRepository::class,
            CartRepository::class,
        )
    }

    @Test
    fun `CartViewModel 생성자의 파라미터 타입을 확인한다`() {
        // given
        val constructor = CartViewModel::class.primaryConstructor!!

        val parameterTypes =
            constructor.parameters.map { parameter ->
                parameter.type.classifier as KClass<*>
            }

        assertThat(parameterTypes).containsExactly(
            CartRepository::class,
        )
    }
}
