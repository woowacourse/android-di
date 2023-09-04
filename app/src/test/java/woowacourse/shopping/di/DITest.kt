package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.FakeApplication
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
class DITest {
    @Test
    fun `MainViewModel을 생성한다`() {
        val vm = FakeApplication.injector.inject<MainViewModel>()

        assertThat(vm).isNotNull
    }

    @Test
    fun `CartViewModel을 생성한다`() {
        val vm = FakeApplication.injector.inject<CartViewModel>()

        assertThat(vm).isNotNull
    }
}
