package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCart
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.RoomCart
import woowacourse.shopping.ui.MainActivity

@RunWith(RobolectricTestRunner::class)
class ShoppingApplicationTest {
    @Test
    fun `앱은 Room과 InMemory 장바구니를 각각 등록한다`() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).setup().get()
        val container = (activity.application as ShoppingApplication).container

        assertThat(container.resolve(CartRepository::class, RoomCart::class))
            .isInstanceOf(DefaultCartRepository::class.java)
        assertThat(container.resolve(CartRepository::class, InMemoryCart::class))
            .isInstanceOf(InMemoryCartRepository::class.java)
        assertThatThrownBy { container.resolve(CartRepository::class) }
            .hasMessageContaining("Qualifier 없이")
    }
}
