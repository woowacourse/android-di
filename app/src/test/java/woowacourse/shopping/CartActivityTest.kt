package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartViewModel

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()

        // then
        assertThat(activity).isNotNull
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull
    }

    @Test
    fun `ViewModel 타입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        assertThat(viewModel).isInstanceOf(CartViewModel::class.java)
    }

    @Test
    fun `ViewModel 은 상품 저장소를 가진다`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()

        // when : ViewModel 생성
        ViewModelProvider(activity)[CartViewModel::class.java]

        val hasCartRepository = CartViewModel::class.java.declaredFields.any {
            CartRepository::class.java.isAssignableFrom(it.type)
        }

        assertThat(hasCartRepository).isTrue
    }
}
