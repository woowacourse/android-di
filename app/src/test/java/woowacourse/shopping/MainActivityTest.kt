package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.Singleton
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
        assertThat(activity).isNotNull()
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
        assertThat(viewModel).isNotNull()
    }

    @Test(expected = IllegalStateException::class)
    fun `Singleton에 CartRepository만 존재하면 ViewModel 생성에 실패한다`() {
        // given
        DependencyInjector.singleton = object : Singleton {
            val cartRepository: CartRepository by lazy { DefaultCartRepository() }
        }

        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()

        // then
        ViewModelProvider(activity)[MainViewModel::class.java]
    }
}
