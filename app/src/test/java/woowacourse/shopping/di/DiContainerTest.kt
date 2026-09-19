package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class DiContainerTest {
    @Test
    fun `ViewModel을 받았을 때 해당 ViewModel이 어떤 클래스인지 알 수 있다`() {
        // given
        val diContainer = DiContainer

        // when
        val productsViewModel = diContainer.createObject(ProductsViewModel::class.java)
        val cartViewModel = diContainer.createObject(CartViewModel::class.java)

        // then
        assertThat(productsViewModel).isInstanceOf(ProductsViewModel::class.java)
        assertThat(cartViewModel).isInstanceOf(CartViewModel::class.java)
    }

    @Test
    fun `ViewModel이 알맞은 파라미터 객체를 찾을 수 있다`() {
        // given
        val diContainer = DiContainer

        // when
        diContainer.objectsMap.clear()
        diContainer.objectsMap[ProductRepository::class.java] = ProductRepository()

        // then
        assertThat(diContainer.hasObject(ProductRepository::class.java)).isTrue()
        assertThat(diContainer.hasObject(CartRepository::class.java)).isFalse()
    }

    @Test
    fun `다른 ViewModel을 만들어도 ViewModel 객체를 생성할 수 있다`() {
        // given
        val diContainer = DiContainer

        // when
        diContainer.objectsMap.clear()
        assertThat(diContainer.createObject(TestViewModel::class.java)).isInstanceOf(TestViewModel::class.java)
    }
}
