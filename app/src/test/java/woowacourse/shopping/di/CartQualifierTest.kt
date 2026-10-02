package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.di.Inject
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.ui.cart.CartViewModel

class InMemoryTarget {
    @field:Inject
    @field:InMemoryCart
    lateinit var repository: CartRepository
}

@RunWith(RobolectricTestRunner::class)
class CartQualifierTest {
    @Test
    fun `앱에 등록한 Room과 In-Memory 구현체를 선택해 주입한다`() {
        assertThat(RuntimeEnvironment.getApplication()).isInstanceOf(ShoppingApplication::class.java)

        val room = HunnitFactory.createInstance(CartViewModel::class).cartRepository
        val inMemory = HunnitFactory.createInstance(InMemoryTarget::class).repository

        assertThat(room).isInstanceOf(DefaultCartRepository::class.java)
        assertThat(inMemory).isInstanceOf(InMemoryCartRepository::class.java)
    }

    @Test
    fun `앱에서 Qualifier 없이 CartRepository를 요청하면 오류를 낸다`() {
        RuntimeEnvironment.getApplication()

        val error =
            org.junit.Assert.assertThrows(IllegalArgumentException::class.java) {
                HunnitFactory.getInstance(CartRepository::class)
            }

        assertThat(error).hasMessageThat().contains("Qualifier를 지정해야 합니다")
    }
}
