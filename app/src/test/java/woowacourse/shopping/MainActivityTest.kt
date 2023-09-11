package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.di.core.DIContainer
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.ViewModelFactory

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        DIContainer.init()
    }

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
        val viewModel = ViewModelFactory().create(MainViewModel::class.java)

        // then
        assertThat(viewModel).isNotNull()
    }
}
