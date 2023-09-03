package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()

        // then
        assertThat(activity).isNotNull
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull
    }

    @Test
    fun `ViewModel 타입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isInstanceOf(MainViewModel::class.java)
    }

    @Test
    fun `ViewModel 은 상품 저장소와 장바구니 저장소를 가진다`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()

        // when : ViewModel 생성
        ViewModelProvider(activity)[MainViewModel::class.java]

        val softly = SoftAssertions().apply {
            // and
            val hasCartRepository = MainViewModel::class.java.declaredFields.any {
                CartRepository::class.java.isAssignableFrom(it.type)
            }

            Assertions.assertThat(hasCartRepository).isTrue

            // and
            val hasProductRepository = MainViewModel::class.java.declaredFields.any {
                ProductRepository::class.java.isAssignableFrom(it.type)
            }

            Assertions.assertThat(hasProductRepository).isTrue
        }
        softly.assertAll()
    }
}
